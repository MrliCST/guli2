package com.atlearn.guli.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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

    public static final String STOCK_EVENT_EXCHANGE = "stock-event-exchange";

    public static final String STOCK_DELAY_QUEUE = "stock-delay-queue";

    public static final String STOCK_RELEASE_QUEUE = "stock-release-queue";

    public static final String STOCK_LOCK_ROUTING_KEY = "stock.lock";

    public static final String STOCK_RELEASE_ROUTING_KEY = "stock.release";

    /** 延迟时间（毫秒），订单超时后自动释放库存，默认 1 分钟 */
    public static final Long TTL = 60000L;

    @Bean
    public DirectExchange stockEventExchange() {
        return ExchangeBuilder.directExchange(STOCK_EVENT_EXCHANGE)
            .durable(true)
            .build();
    }

    /**
     * 延迟队列：消息进入后等待 TTL 过期，变成死信转发回交换机
     */
    @Bean
    public Queue stockDelayQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", TTL);
        args.put("x-dead-letter-exchange", STOCK_EVENT_EXCHANGE);
        args.put("x-dead-letter-routing-key", STOCK_RELEASE_ROUTING_KEY);
        return QueueBuilder.durable(STOCK_DELAY_QUEUE)
            .withArguments(args)
            .build();
    }

    /**
     * 消费队列：接收延迟队列过期后转发的死信消息
     */
    @Bean
    public Queue stockReleaseQueue() {
        return QueueBuilder.durable(STOCK_RELEASE_QUEUE)
            .build();
    }

    @Bean
    public Binding stockDelayBinding(Queue stockDelayQueue, DirectExchange stockEventExchange) {
        return BindingBuilder.bind(stockDelayQueue)
            .to(stockEventExchange)
            .with(STOCK_LOCK_ROUTING_KEY);
    }

    @Bean
    public Binding stockReleaseBinding(Queue stockReleaseQueue, DirectExchange stockEventExchange) {
        return BindingBuilder.bind(stockReleaseQueue)
            .to(stockEventExchange)
            .with(STOCK_RELEASE_ROUTING_KEY);
    }
}
