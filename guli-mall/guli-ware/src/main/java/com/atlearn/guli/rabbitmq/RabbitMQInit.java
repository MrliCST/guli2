package com.atlearn.guli.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import com.atlearn.guli.constant.RabbitMqConstant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 拓扑初始化
 *
 * 交换机 ──stock.lock──→ 延迟队列(TTL + 死信)
 *                              │ 消息过期变死信
 * 交换机 ←──stock.release─── 死信路由
 *    └──stock.release──→ 消费队列 ──→ Listener
 *
 * @author guli
 */
@Configuration
public class RabbitMQInit {

    /**
     * stock-event-exchange 交换机
     * @return
     */
    @Bean
    public DirectExchange stockEventExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.STOCK_EVENT_EXCHANGE)
            .durable(true)
            .build();
    }

    /**
     * stock-delay-queue 延迟队列
     * 延迟队列：消息进入后等待 TTL 过期，变成死信转发回交换机
     */
    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", RabbitMqConstant.TTL);
        args.put("x-dead-letter-exchange", RabbitMqConstant.STOCK_EVENT_EXCHANGE);
        args.put("x-dead-letter-routing-key", RabbitMqConstant.STOCK_RELEASE_ROUTING_KEY);
        return QueueBuilder.durable(RabbitMqConstant.STOCK_DELAY_QUEUE)
            .withArguments(args)
            .build();
    }

    /**
     * stock-release-queue 消费队列 
     * 消费队列：接收延迟队列过期后转发的死信消息
     */
    @Bean
    public Queue stockReleaseQueue() {
        return QueueBuilder.durable(RabbitMqConstant.STOCK_RELEASE_QUEUE)
            .build();
    }

    @Bean
    public Binding stockDelayBinding(@Qualifier("stockDelayQueue") Queue stockDelayQueue,
                                     @Qualifier("stockEventExchange") DirectExchange stockEventExchange) {
        return BindingBuilder.bind(stockDelayQueue)  // 队列
            .to(stockEventExchange)  // 路由
            .with(RabbitMqConstant.STOCK_LOCK_ROUTING_KEY); // 路由键
    }

    @Bean
    public Binding stockReleaseBinding(@Qualifier("stockReleaseQueue") Queue stockReleaseQueue,
                                       @Qualifier("stockEventExchange") DirectExchange stockEventExchange) {
        return BindingBuilder.bind(stockReleaseQueue)  // 队列
            .to(stockEventExchange)  // 路由
            .with(RabbitMqConstant.STOCK_RELEASE_ROUTING_KEY);  // 路由键
    }
}
