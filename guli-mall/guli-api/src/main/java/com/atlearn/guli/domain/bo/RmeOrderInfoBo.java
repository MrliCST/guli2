package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单冗余信息（用于库存工作单主表 wms_ware_order_task）
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RmeOrderInfoBo {

    /** 订单 id */
    private Long orderId;

    /** 订单号 */
    private String orderSn;

    /** 收货人 */
    private String consignee;

    /** 收货人电话 */
    private String consigneeTel;

    /** 配送地址 */
    private String deliveryAddress;

    /** 付款方式【 1:在线付款 2:货到付款】 */
    private Integer paymentWay;

    /** 订单描述 */
    private String orderBody;
}
