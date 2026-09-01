package com.atlearn.guli.domain.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单 MQ 传输对象
 * 用于 RabbitMQ 延迟消息传递（订单超时自动取消），不含 MyBatis-Plus 注解和 BaseEntity 审计字段
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RmeOrderTo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单 id
     */
    private Long id;

    /**
     * 订单号（MQ 中真正标识订单的唯一字段）
     */
    private String orderSn;

    /**
     * 会员 id
     */
    private Long memberId;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 应付总额
     */
    private BigDecimal payAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;
}
