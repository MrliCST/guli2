package com.atlearn.guli.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author guli
 */
@Getter
public enum OrderStatusEnum {

    /** 待付款 */
    UNPAID(0, "待付款"),

    /** 待发货 */
    WAIT_DELIVER(1, "待发货"),

    /** 已发货 */
    SHIPPED(2, "已发货"),

    /** 已完成 */
    COMPLETED(3, "已完成"),

    /** 已关闭 */
    CLOSED(4, "已关闭"),

    /** 无效订单 */
    INVALID(5, "无效订单");

    private final Integer code;
    private final String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
