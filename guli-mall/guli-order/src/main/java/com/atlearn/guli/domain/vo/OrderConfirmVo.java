package com.atlearn.guli.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页数据
 *
 * @author guli
 */
@Data
@Builder
public class OrderConfirmVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收货地址列表
     */
    private List<RmeMemberReceiveAddressVO> memberReceiveAddressList;

    /**
     * 勾选的购物车项列表
     */
    private List<RmeCartItemVo> cartItemList;

    /**
     * 订单总价
     */
    private BigDecimal totalAmount;

    /**
     * 应付总价
     */
    private BigDecimal payAmount;

    /**
     * 防重令牌
     */
    private String orderToken;

}
