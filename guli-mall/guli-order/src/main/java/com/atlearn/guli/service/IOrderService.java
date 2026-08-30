package com.atlearn.guli.service;

import com.atlearn.guli.domain.vo.OrderConfirmVo;

import java.util.List;

/**
 * 订单服务接口
 *
 * @author guli
 */
public interface IOrderService {

    /**
     * 订单确认页数据
     *
     * @param skuIds 用户勾选的购物车项 skuId 列表
     * @return 订单确认页数据
     */
    OrderConfirmVo confirmOrder(List<Long> skuIds);

}
