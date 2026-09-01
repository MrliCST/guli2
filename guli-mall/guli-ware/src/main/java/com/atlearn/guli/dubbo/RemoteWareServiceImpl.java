package com.atlearn.guli.dubbo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.domain.WmsWareOrderTask;
import com.atlearn.guli.domain.WmsWareOrderTaskDetail;
import com.atlearn.guli.domain.bo.RmeLockWareBo;
import com.atlearn.guli.domain.bo.RmeOrderInfoBo;
import com.atlearn.guli.domain.dto.LockItemDto;
import com.atlearn.guli.domain.mq.RmeWareOrderTask;
import com.atlearn.guli.domain.vo.RmeWareSkuVo;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.mapper.WmsWareLockerMapper;
import com.atlearn.guli.mapper.WmsWareOrderTaskDetailMapper;
import com.atlearn.guli.mapper.WmsWareOrderTaskMapper;
import com.atlearn.guli.mapper.WmsWareSkuMapper;

import com.atlearn.guli.constant.RabbitMqConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    private final WmsWareLockerMapper wmsWareLockerMapper;
    private final RabbitTemplate rabbitTemplate;

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
     * 批量CAS锁定库存
     * 任意一条失败（0=库存不足）则抛异常，@Transactional 回滚全部
     * 锁定成功后插入库存工作单（wms_ware_order_task）和明细（wms_ware_order_task_detail）
     * MQ 延迟消息在事务 afterCommit 后发送，避免回滚后消息已发
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean lockWareSkuBatch(RmeOrderInfoBo orderInfo, List<RmeLockWareBo> lockWareBoList) {
        if (lockWareBoList == null || lockWareBoList.isEmpty()) {
            return true;
        }

        // 1. 展平并按 (wareId, skuId) 升序排序（保证并发加锁顺序一致，防死锁）
        List<LockItemDto> items = lockWareBoList.stream()
            .flatMap(bo -> bo.getWareDistribute().stream()
                .map(dist -> LockItemDto.builder()
                    .wareId(dist.getWareId())
                    .skuId(bo.getSkuId())
                    .lockNum(dist.getLockNum())
                    .skuName(bo.getSkuName())
                    .build()))
            .sorted(Comparator.comparing(LockItemDto::getWareId)
                .thenComparing(LockItemDto::getSkuId))
            .collect(Collectors.toList());

        // 2. 批量 CAS 锁定库存
        int[] updateCounts = wmsWareLockerMapper.batchLockStock(items);

        // 3. 校验锁定结果
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] <= 0) {
                LockItemDto failedItem = items.get(i);
                log.error("库存锁定失败，库存不足或记录不存在: wareId={}, skuId={}, lockNum={}",
                    failedItem.getWareId(), failedItem.getSkuId(), failedItem.getLockNum());
                throw new BusinessException(ErrorCodeEnum.STOCK_NOT_ENOUGH);
            }
        }

        // 4. 插入库存工作单主表
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

        // 5. 插入库存工作单明细
        List<WmsWareOrderTaskDetail> detailList = items.stream()
            .map(item -> WmsWareOrderTaskDetail.builder()
                .skuId(item.getSkuId())
                .skuName(item.getSkuName())
                .lockNum(item.getLockNum())
                .taskId(task.getId())
                .wareId(item.getWareId())
                .lockStatus(1)
                .build())
            .collect(Collectors.toList());
        wareOrderTaskDetailMapper.insertBatch(detailList);

        // 6. 事务提交成功后再发送 MQ 延迟消息，避免回滚后消息已发
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                RmeWareOrderTask mqTask = new RmeWareOrderTask();
                BeanUtils.copyProperties(task, mqTask);
                rabbitTemplate.convertAndSend(
                    RabbitMqConstant.STOCK_EVENT_EXCHANGE,
                    RabbitMqConstant.STOCK_LOCK_ROUTING_KEY,
                    mqTask
                );
            }
        });

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

}
