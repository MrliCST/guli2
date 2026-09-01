package com.atlearn.guli.constant;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * 订单相关常量
 *
 * @author guli
 */
public class OrderConstant {

    private OrderConstant() {
    }

    /** 防重令牌 Redis key 前缀 */
    public static final String ORDER_TOKEN_PREFIX = "guli:order:token:";

    /** 防重令牌有效期 30分钟 */
    public static final Duration ORDER_TOKEN_TTL = Duration.ofMinutes(30);

    /** 价格校验阈值（差值超过此值认为价格变动） */
    public static final BigDecimal PRICE_DIFF_THRESHOLD = new BigDecimal("0.01");

    /** 订单来源：PC */
    public static final Integer SOURCE_TYPE_PC = 0;

    /** 订单来源：APP */
    public static final Integer SOURCE_TYPE_APP = 1;

    /** 删除状态：未删除 */
    public static final Integer DELETE_STATUS_NORMAL = 0;

    /** 确认状态：未确认 */
    public static final Integer CONFIRM_STATUS_NORMAL = 0;

    /** 订单状态：待付款 */
    public static final Integer ORDER_STATUS_PENDING_PAYMENT = 0;

    /** 订单状态：已付款 */
    public static final Integer ORDER_STATUS_PAID = 1;

    /** 订单状态：已发货 */
    public static final Integer ORDER_STATUS_SHIPPED = 2;

    /** 订单状态：已完成 */
    public static final Integer ORDER_STATUS_COMPLETED = 3;

    /** 订单状态：已取消 */
    public static final Integer ORDER_STATUS_CANCELLED = 4;

    /** 订单状态：售后中 */
    public static final Integer ORDER_STATUS_AFTER_SALE = 5;

    /** 订单状态：售后完成 */
    public static final Integer ORDER_STATUS_AFTER_SALE_DONE = 6;
}
