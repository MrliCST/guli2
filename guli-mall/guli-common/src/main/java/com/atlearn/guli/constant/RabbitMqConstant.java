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

    // ==================== 订单事件交换机/队列/路由键 ====================

    /** 订单事件交换机 */
    public static final String ORDER_EVENT_EXCHANGE = "order-event-exchange";

    /** 订单延迟队列（TTL + 死信） */
    public static final String ORDER_DELAY_QUEUE = "order-delay-queue";

    /** 订单释放消费队列 */
    public static final String ORDER_RELEASE_QUEUE = "order-release-queue";

    /** 创建订单路由键 */
    public static final String ORDER_CREATE_ROUTING_KEY = "order.create";

    /** 释放订单路由键（死信转发用） */
    public static final String ORDER_RELEASE_ROUTING_KEY = "order.release";

    /** 订单超时时间（毫秒），默认 30 分钟未支付自动取消 */
    public static final Long ORDER_TTL = 30 * 60 * 1000L;
    /** 订单释放其他路由键（用于库存服务监听订单释放消息） */
    public static final String ORDER_RELEASE_OTHER_ROUTING_KEY = "order.release.other";
}
