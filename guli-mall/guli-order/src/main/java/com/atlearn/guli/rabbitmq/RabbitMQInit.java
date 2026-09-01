package com.atlearn.guli.rabbitmq;

import com.atlearn.guli.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单 RabbitMQ 拓扑初始化
 *
 * 交换机 ──order.create──→ 延迟队列(TTL + 死信)
 *                              │ 消息过期变死信
 * 交换机 ←──order.release─── 死信路由
 *    └──order.release──→ 消费队列 ──→ Listener（超时取消订单）
 *
 * @author guli
 */
@Configuration
public class RabbitMQInit {

    /**
     * order-event-exchange 交换机
     */
    @Bean
    public DirectExchange orderEventExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.ORDER_EVENT_EXCHANGE)
            .durable(true)
            .build();
    }

    /**
     * order-delay-queue 延迟队列
     * 延迟队列：消息进入后等待 TTL 过期，变成死信转发回交换机
     */
    @Bean
    public Queue orderDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", RabbitMqConstant.ORDER_TTL);
        args.put("x-dead-letter-exchange", RabbitMqConstant.ORDER_EVENT_EXCHANGE);
        args.put("x-dead-letter-routing-key", RabbitMqConstant.ORDER_RELEASE_ROUTING_KEY);
        return QueueBuilder.durable(RabbitMqConstant.ORDER_DELAY_QUEUE)
            .withArguments(args)
            .build();
    }

    /**
     * order-release-queue 消费队列
     * 消费队列：接收延迟队列过期后转发的死信消息
     */
    @Bean
    public Queue orderReleaseQueue() {
        return QueueBuilder.durable(RabbitMqConstant.ORDER_RELEASE_QUEUE)
            .build();
    }

    @Bean
    public Binding orderDelayBinding(@Qualifier("orderDelayQueue") Queue orderDelayQueue,
                                     @Qualifier("orderEventExchange") DirectExchange orderEventExchange) {
        return BindingBuilder.bind(orderDelayQueue)
            .to(orderEventExchange)
            .with(RabbitMqConstant.ORDER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding orderReleaseBinding(@Qualifier("orderReleaseQueue") Queue orderReleaseQueue,
                                       @Qualifier("orderEventExchange") DirectExchange orderEventExchange) {
        return BindingBuilder.bind(orderReleaseQueue)
            .to(orderEventExchange)
            .with(RabbitMqConstant.ORDER_RELEASE_ROUTING_KEY);
    }

    /**
     * 库存释放消费队列（与库存服务共用，此处声明确保绑定可用）
     */
    @Bean
    public Queue stockReleaseQueue() {
        return QueueBuilder.durable(RabbitMqConstant.STOCK_RELEASE_QUEUE)
            .build();
    }

    /**
     * 订单取消后，通过 order-event-exchange 路由到 stock-release-queue，主动触发库存解锁
     */
    @Bean
    public Binding orderReleaseStockBinding(@Qualifier("stockReleaseQueue") Queue stockReleaseQueue,
                                            @Qualifier("orderEventExchange") DirectExchange orderEventExchange) {
        return BindingBuilder.bind(stockReleaseQueue)
            .to(orderEventExchange)
            .with(RabbitMqConstant.ORDER_RELEASE_OTHER_ROUTING_KEY);
    }
}
