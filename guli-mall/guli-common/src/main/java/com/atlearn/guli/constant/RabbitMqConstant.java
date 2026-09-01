package com.atlearn.guli.constant;

/**
 * RabbitMQ 常量
 *
 * @author guli
 */
public class RabbitMqConstant {

    /** 库存事件交换机 */
    public static final String STOCK_EVENT_EXCHANGE = "stock-event-exchange";

    /** 库存延迟队列（TTL + 死信） */
    public static final String STOCK_DELAY_QUEUE = "stock-delay-queue";

    /** 库存释放消费队列 */
    public static final String STOCK_RELEASE_QUEUE = "stock-release-queue";

    /** 锁定库存路由键 */
    public static final String STOCK_LOCK_ROUTING_KEY = "stock.lock";

    /** 释放库存路由键 */
    public static final String STOCK_RELEASE_ROUTING_KEY = "stock.release";

    /** 延迟时间（毫秒），订单超时后自动释放库存，默认 1 分钟 */
    public static final Long TTL = 60000L;
}
