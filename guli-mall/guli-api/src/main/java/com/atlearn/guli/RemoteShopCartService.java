package com.atlearn.guli;

import com.atlearn.guli.domain.vo.RmeCartItemVo;

import java.util.List;

/**
 * 远程购物车服务
 * RemoteShopCartService
 */
public interface RemoteShopCartService {

    /**
     * 获取当前用户购物车项列表
     *
     * @return 购物车项列表
     */
    List<RmeCartItemVo> getCartItemList();

}
