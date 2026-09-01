package com.atlearn.guli.rabbitmq;

import com.atlearn.guli.constant.OrderConstant;
import com.atlearn.guli.constant.RabbitMqConstant;
import com.atlearn.guli.domain.OmsOrder;
import com.atlearn.guli.domain.mq.RmeOrderTo;
import com.atlearn.guli.domain.mq.RmeWareOrderTask;
import com.atlearn.guli.mapper.OmsOrderMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.util.Date;

/**
 * 订单 RabbitMQ 消费者
 *
 * @author guli
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final OmsOrderMapper omsOrderMapper;
    private final RabbitTemplate rabbitTemplate;

    @SuppressWarnings("null")
    @RabbitListener(queues = RabbitMqConstant.ORDER_RELEASE_QUEUE)
    public void handleOrderRelease(RmeOrderTo orderTo, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("收到订单释放消息: orderSn={}", orderTo.getOrderSn());
        try {
            cancelOrderIfPending(orderTo.getOrderSn());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("订单释放处理失败: orderSn={}", orderTo.getOrderSn(), e);
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 若订单仍处于待付款状态，则取消订单，并主动通知库存释放
     */
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("null")//null抑制警告
    public void cancelOrderIfPending(String orderSn) {
        // 直接带状态条件更新，数据库层面原子判断，避免先查后改的并发问题
        OmsOrder update = OmsOrder.builder()
            .status(OrderConstant.ORDER_STATUS_CANCELLED)
            .modifyTime(new Date())
            .build();

        int rows = omsOrderMapper.update(update,
            Wrappers.<OmsOrder>lambdaUpdate()
                .eq(OmsOrder::getOrderSn, orderSn)
                .eq(OmsOrder::getStatus, OrderConstant.ORDER_STATUS_PENDING_PAYMENT)
        );

        if (rows == 0) {
            // 更新行数为 0，说明订单不存在或状态不是待付款，补充查询仅用于日志定位
            OmsOrder order = omsOrderMapper.selectOne(
                Wrappers.<OmsOrder>lambdaQuery()
                    .eq(OmsOrder::getOrderSn, orderSn)
                    .last("LIMIT 1")
            );
            if (order == null) {
                log.warn("订单不存在，跳过取消: orderSn={}", orderSn);
            } else {
                log.info("订单状态非待付款，跳过取消: orderSn={}, status={}", orderSn, order.getStatus());
            }
            return;
        }

        log.info("订单超时自动取消成功: orderSn={}", orderSn);

        // 事务提交后再发送库存释放消息，避免事务回滚但消息已发出导致的不一致
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                rabbitTemplate.convertAndSend(
                    RabbitMqConstant.ORDER_EVENT_EXCHANGE,
                    RabbitMqConstant.ORDER_RELEASE_OTHER_ROUTING_KEY,
                    RmeWareOrderTask.builder().orderSn(orderSn).build()
                );
                log.info("已发送库存释放通知: orderSn={}", orderSn);
            }
        });
    }
}
