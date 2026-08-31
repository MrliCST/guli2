package com.atlearn.guli.dubbo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.domain.WmsWareOrderTask;
import com.atlearn.guli.domain.WmsWareOrderTaskDetail;
import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.domain.bo.RmeWareSkuLockBo;
import com.atlearn.guli.domain.bo.RmeWareSkuLockItemBo;
import com.atlearn.guli.domain.vo.RmeWareStockLockResultVo;
import com.atlearn.guli.mapper.WmsWareOrderTaskDetailMapper;
import com.atlearn.guli.mapper.WmsWareOrderTaskMapper;
import com.atlearn.guli.mapper.WmsWareSkuMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteWareServiceImpl implements RemoteWareService {

    /** 锁定状态：已锁定 */
    private static final int LOCK_STATUS_LOCKED = 1;

    /** 工作单状态：已锁定 */
    private static final int TASK_STATUS_LOCKED = 1;

    // 分元转换 | 1元 = 100分
    private static final Long CENTS_PER_YUAN = 100L;

    private final WmsWareSkuMapper wareSkuMapper;
    private final WmsWareOrderTaskMapper wareOrderTaskMapper;
    private final WmsWareOrderTaskDetailMapper wareOrderTaskDetailMapper;

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

    /**
     * 已锁定记录（用于失败回滚）
     */
    @Data
    @AllArgsConstructor
    private static class LockRecord {
        private Long wareSkuId;
        private Long wareId;
        private Long skuId;
        private String skuName;
        private Integer skuNum;
    }

    /**
     * 订单锁库存
     * 算法：遍历每件商品 → 查询该sku所有仓库 → 遍历仓库乐观锁UPDATE → 成功跳下一个商品，失败换下一个仓库
     * 任一sku所有仓库失败 → 释放前面已锁住的所有库存 → 返回失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RmeWareStockLockResultVo orderLockStock(RmeWareSkuLockBo lockBo) {
        String orderSn = lockBo.getOrderSn();
        List<RmeWareSkuLockItemBo> lockItems = lockBo.getLockItems();

        if (lockItems == null || lockItems.isEmpty()) {
            return RmeWareStockLockResultVo.fail("锁定商品列表为空");
        }

        // 记录已锁定的明细，用于失败回滚
        List<LockRecord> lockedRecords = new ArrayList<>();

        try {
            // 1. 遍历每个要锁定的sku
            for (RmeWareSkuLockItemBo item : lockItems) {
                Long skuId = item.getSkuId();
                String skuName = item.getSkuName();
                int num = item.getSkuNum();

                if (num <= 0) {
                    continue;
                }

                // 2. 查询该sku所有有库存的仓库（按可用库存倒序，优先用库存多的）
                List<WmsWareSku> wareSkuList = wareSkuMapper.listWareBySkuId(skuId);

                if (wareSkuList == null || wareSkuList.isEmpty()) {
                    return RmeWareStockLockResultVo.fail("商品[" + skuName + "]库存不足");
                }

                // 3. 遍历仓库，尝试乐观锁锁定
                boolean locked = false;
                for (WmsWareSku wareSku : wareSkuList) {
                    int rows = wareSkuMapper.lockStock(wareSku.getId(), (long) num);
                    if (rows > 0) {
                        // 锁定成功
                        lockedRecords.add(new LockRecord(
                            wareSku.getId(),
                            wareSku.getWareId(),
                            skuId,
                            skuName,
                            num
                        ));
                        locked = true;
                        break;
                    }
                }

                // 4. 所有仓库都锁失败 → 回滚
                if (!locked) {
                    // 回滚已锁定的库存
                    rollbackLockedStock(lockedRecords);
                    return RmeWareStockLockResultVo.fail("商品[" + skuName + "]库存不足");
                }
            }

            // 5. 全部锁定成功 → 创建库存工作单和详情
            WmsWareOrderTask orderTask = new WmsWareOrderTask();
            orderTask.setOrderSn(orderSn);
            orderTask.setTaskStatus(TASK_STATUS_LOCKED);
            wareOrderTaskMapper.insert(orderTask);

            for (LockRecord record : lockedRecords) {
                WmsWareOrderTaskDetail detail = new WmsWareOrderTaskDetail();
                detail.setTaskId(orderTask.getId());
                detail.setSkuId(record.getSkuId());
                detail.setSkuName(record.getSkuName());
                detail.setSkuNum(record.getSkuNum());
                detail.setWareId(record.getWareId());
                detail.setLockStatus(LOCK_STATUS_LOCKED);
                wareOrderTaskDetailMapper.insert(detail);
            }

            log.info("订单[{}]库存锁定成功，共锁定{}件商品", orderSn, lockedRecords.size());
            return RmeWareStockLockResultVo.ok();

        } catch (Exception e) {
            log.error("订单[{}]库存锁定异常", orderSn, e);
            // 异常时也回滚
            rollbackLockedStock(lockedRecords);
            return RmeWareStockLockResultVo.fail("库存锁定异常：" + e.getMessage());
        }
    }

    /**
     * 回滚已锁定的库存
     */
    private void rollbackLockedStock(List<LockRecord> lockedRecords) {
        if (lockedRecords == null || lockedRecords.isEmpty()) {
            return;
        }
        for (LockRecord record : lockedRecords) {
            try {
                wareSkuMapper.unlockStock(record.getWareSkuId(), (long) record.getSkuNum());
            } catch (Exception e) {
                log.error("库存回滚失败，wareSkuId={}, skuId={}, num={}",
                    record.getWareSkuId(), record.getSkuId(), record.getSkuNum(), e);
            }
        }
    }

}
