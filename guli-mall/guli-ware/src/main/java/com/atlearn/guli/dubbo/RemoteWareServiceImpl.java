package com.atlearn.guli.dubbo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.domain.WmsWareOrderTask;
import com.atlearn.guli.domain.WmsWareOrderTaskDetail;
import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.domain.bo.RmeLockWareBo;
import com.atlearn.guli.domain.bo.RmeOrderInfoBo;
import com.atlearn.guli.domain.vo.RmeWareSkuVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.mapper.WmsWareOrderTaskDetailMapper;
import com.atlearn.guli.mapper.WmsWareOrderTaskMapper;
import com.atlearn.guli.mapper.WmsWareSkuMapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteWareServiceImpl implements RemoteWareService {

    // 分元转换 | 1元 = 100分
    private static final Long CENTS_PER_YUAN = 100L;

    private final WmsWareSkuMapper wareSkuMapper;
    private final WmsWareOrderTaskMapper wareOrderTaskMapper;
    private final WmsWareOrderTaskDetailMapper wareOrderTaskDetailMapper;
    private final DataSource dataSource;

    /**
     * 查询sku商品，对应的总可用库存
     */
    @Override
    public Map<Long, Long> getSkuAvailableStock(List<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // DTO转map，key为skuId，value为总可用库存
        Map<Long, Long> stockMap = wareSkuMapper.getSkuIdToStockMap(skuIds).stream()
            .collect(Collectors.toMap(dto -> dto.getSkuId(), dto -> dto.getStock()));

        return skuIds.stream()
            .collect(Collectors.toMap(
                Function.identity(),  // 恒等映射 x -> x
                skuId -> stockMap.getOrDefault(skuId, 0L)
            ));
    }

    /**
     * 批量查询sku对应的各个仓库的可用库存信息
     */
    @Override
    public List<RmeWareSkuVo> getWareSkuListBySkuIds(List<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return Collections.emptyList();
        }
        return wareSkuMapper.getWareSkuListBySkuIds(skuIds);
    }

    /**
     * 批量CAS锁定库存（JDBC addBatch + 排序防死锁）
     * 任意一条失败（0=库存不足）则抛异常，@Transactional 回滚全部
     * 锁定成功后插入库存工作单（wms_ware_order_task）和明细（wms_ware_order_task_detail）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean lockWareSkuBatch(RmeOrderInfoBo orderInfo, List<RmeLockWareBo> lockWareBoList) {
        if (lockWareBoList == null || lockWareBoList.isEmpty()) {
            return true;
        }

        // 1. 收集 skuId，查询 sku_name
        List<Long> skuIds = lockWareBoList.stream()
            .map(RmeLockWareBo::getSkuId)
            .collect(Collectors.toList());
        Map<Long, String> skuNameMap = wareSkuMapper.selectList(
            new LambdaQueryWrapper<WmsWareSku>().in(WmsWareSku::getSkuId, skuIds)
        ).stream()
            .collect(Collectors.toMap(
                WmsWareSku::getSkuId,
                WmsWareSku::getSkuName,
                (existing, replacement) -> existing
            ));

        // 2. 展平并按 wareId, skuId 升序排序（保证并发加锁顺序一致，防死锁）
        List<LockItem> items = lockWareBoList.stream()
            .flatMap(bo -> bo.getWareDistribute().stream()
                .map(dist -> new LockItem(dist.getWareId(), bo.getSkuId(), dist.getLockNum())))
            .sorted(Comparator.comparing(LockItem::getWareId)
                .thenComparing(LockItem::getSkuId))
            .collect(Collectors.toList());

        String sql = "UPDATE wms_ware_sku SET stock_locked = stock_locked + ? "
                   + "WHERE ware_id = ? AND sku_id = ? AND (stock - stock_locked) >= ?";

        // 3. 标准 JDBC Batch 执行
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (LockItem item : items) {
                ps.setInt(1, item.getLockNum());
                ps.setLong(2, item.getWareId());
                ps.setLong(3, item.getSkuId());
                ps.setInt(4, item.getLockNum());
                ps.addBatch();
            }

            int[] updateCounts = ps.executeBatch();

            // 4. 校验每条结果
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] <= 0 && updateCounts[i] != Statement.SUCCESS_NO_INFO) {
                    log.error("库存锁定失败，第{}条UPDATE未命中/库存不足，wareId={}，skuId={}",
                        i + 1, items.get(i).getWareId(), items.get(i).getSkuId());
                    throw new BusinessException(ErrorCodeEnum.STOCK_NOT_ENOUGH);
                }
            }
        } catch (SQLException e) {
            log.error("批量锁定库存SQL执行失败", e);
            throw new BusinessException(ErrorCodeEnum.STOCK_NOT_ENOUGH);
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }

        // 5. 插入库存工作单主表
        WmsWareOrderTask task = WmsWareOrderTask.builder()
            .orderId(orderInfo.getOrderId())
            .orderSn(orderInfo.getOrderSn())
            .consignee(orderInfo.getConsignee())
            .consigneeTel(orderInfo.getConsigneeTel())
            .deliveryAddress(orderInfo.getDeliveryAddress())
            .paymentWay(orderInfo.getPaymentWay())
            .orderBody(orderInfo.getOrderBody())
            .taskStatus(1)
            .build();
        wareOrderTaskMapper.insert(task);

        // 6. 插入库存工作单明细
        List<WmsWareOrderTaskDetail> detailList = items.stream()
            .map(item -> WmsWareOrderTaskDetail.builder()
                .skuId(item.getSkuId())
                .skuName(skuNameMap.get(item.getSkuId()))
                .lockNum(item.getLockNum())
                .taskId(task.getId())
                .wareId(item.getWareId())
                .lockStatus(1)
                .build())
            .collect(Collectors.toList());
        wareOrderTaskDetailMapper.insertBatch(detailList);

        return true;
    }

    /**
     * 模拟获取运费
     * @param ReceiverAddressInfo 收货地址信息
     */
    @Override
    public BigDecimal getShippingFee(String ReceiverAddressInfo) {
        long base = ReceiverAddressInfo.length();
        long seed = (base * 423142070409L + 202608301740L) ^ 20041123L;
        long cents = base * CENTS_PER_YUAN + (Math.abs(seed) % 1953L);  // 单位为分

        // 分转为元，保留两位小数
        return BigDecimal.valueOf(cents).divide(BigDecimal.valueOf(CENTS_PER_YUAN), 2, RoundingMode.HALF_UP);
    }

    /** 展平后的锁定项，用于排序 */
    @Data
    @AllArgsConstructor
    private static class LockItem {
        private Long wareId;
        private Long skuId;
        private Integer lockNum;
    }

}
