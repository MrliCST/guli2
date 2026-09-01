package com.atlearn.guli.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 消费者：监听库存释放队列
 *
 * @author guli
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMQInit.STOCK_RELEASE_QUEUE)
public class RabbitMQListener {

    @RabbitHandler
    public void handleStockRelease(String message) {
        log.info("收到库存释放消息: {}", message);
        // TODO 解析消息，调用 WareSkuService 释放锁定的库存
    }
}
