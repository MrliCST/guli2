package com.atlearn.guli.service;

import com.atlearn.guli.domain.CartItem;

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
    void addCart(CartItem cartItem);

}
