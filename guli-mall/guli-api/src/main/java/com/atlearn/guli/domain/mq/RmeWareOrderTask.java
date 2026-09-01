package com.atlearn.guli.domain.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 库存工作单 MQ 传输对象
 * 用于 RabbitMQ 延迟消息传递，不含 MyBatis-Plus 注解和 BaseEntity 审计字段
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RmeWareOrderTask implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * order_id : 在订单入库前为 null
     */
    private Long orderId;

    /**
     * order_sn : mq消息中真正标识订单的唯一字段
     */
    private String orderSn;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String consigneeTel;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 订单备注
     */
    private String orderComment;

    /**
     * 付款方式【 1:在线付款 2:货到付款】
     */
    private Integer paymentWay;

    /**
     * 任务状态 1-已锁定 2-已解锁 3-扣减
     */
    private Integer taskStatus;

    /**
     * 订单描述
     */
    private String orderBody;

    /**
     * 物流单号
     */
    private String trackingNo;

    /**
     * 工作单备注
     */
    private String taskComment;
}
