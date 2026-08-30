package com.atlearn.guli.service;

import com.atlearn.guli.domain.ShopCart;

/**
 * 购物车服务接口
 *
 * @author guli
 */
public interface ICartService {

    /**
     * 添加商品到购物车
     *
     * @param cartItem 购物车项
     */
    void addToCart(Long skuId);

    /**
     * 更新购物车商品数量
     *
     * @param skuId skuId
     * @param type  操作类型：incr 加一 / decr 减一
     */
    void updateForCart(Long skuId, String type);

    /**
     * 从购物车删除商品
     *
     * @param skuId skuId
     */
    void deleteFromCart(Long skuId);

    /**
     * 查看购物车
     *
     * @return 购物车对象
     */
    ShopCart getCart();

}
