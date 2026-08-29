package com.atlearn.guli.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 * Map<用户标识: Map<skuid,CartItem>>
 *
 * @author guli
 */
@Data
public class ShopCart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 购物车项列表
     */
    private List<CartItem> items;

    /**
     * 商品总数量
     */
    private Integer countNum;

    /**
     * 商品类型数量
     */
    private Integer countType;

    /**
     * 商品总金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠减免金额
     */
    private BigDecimal reduce;

}
