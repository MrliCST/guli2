package com.atlearn.guli.rabbitmq;

import com.atlearn.guli.RemoteOrderService;
import com.atlearn.guli.constant.OrderConstant;
import com.atlearn.guli.constant.RabbitMqConstant;
import com.atlearn.guli.domain.WmsWareOrderTaskDetail;
import com.atlearn.guli.domain.dto.LockItemDto;
import com.atlearn.guli.domain.mq.RmeWareOrderTask;
import com.atlearn.guli.mapper.WmsWareLockerMapper;
import com.atlearn.guli.mapper.WmsWareOrderTaskDetailMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RabbitMQ 消费者
 *
 * @author guli
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @DubboReference
    private RemoteOrderService remoteOrderService;

    private final WmsWareLockerMapper wmsWareLockerMapper;
    private final WmsWareOrderTaskDetailMapper wmsWareOrderTaskDetailMapper;

    @RabbitListener(queues = RabbitMqConstant.STOCK_RELEASE_QUEUE)
    public void handleStockRelease(RmeWareOrderTask task, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("收到库存释放消息: taskId={}, orderSn={}", task.getId(), task.getOrderSn());
        try {
            // 查询订单状态
            Integer orderStatus = remoteOrderService.getOrderStatusBySn(task.getOrderSn());

            // 订单不存在/已取消 → 释放库存
            if (orderStatus == null || orderStatus.equals(OrderConstant.ORDER_STATUS_CANCELLED)) {
                //  查询工作单明细
                List<WmsWareOrderTaskDetail> detailList = wmsWareOrderTaskDetailMapper.selectList(
                    Wrappers.<WmsWareOrderTaskDetail>lambdaQuery()
                        .eq(WmsWareOrderTaskDetail::getTaskId, task.getId())
                );

                //  转为 DTO + 排序防死锁
                List<LockItemDto> items = detailList.stream()
                    .map(detail -> LockItemDto.builder()
                        .wareId(detail.getWareId())
                        .skuId(detail.getSkuId())
                        .lockNum(detail.getLockNum())
                        .skuName(detail.getSkuName())
                        .build())
                    .sorted(Comparator.comparing(LockItemDto::getWareId)
                        .thenComparing(LockItemDto::getSkuId))
                    .collect(Collectors.toList());

                int[] updateCounts = wmsWareLockerMapper.batchUnlockStock(items);

                // 任一条明细解锁失败（影响行数为 0）即抛异常，触发消息拒收重新入队
                for (int updateCount : updateCounts) {
                    if (updateCount == 0) {
                        throw new IllegalStateException("库存解锁失败，存在未解锁明细: taskId=" + task.getId());
                    }
                }

                log.info("库存解锁成功: taskId={}, 明细数={}", task.getId(), items.size());
            }

            // 消费消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("库存释放处理失败: taskId={}", task.getId(), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
