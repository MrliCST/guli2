package com.atlearn.guli.service;

import com.atlearn.guli.domain.OmsOrder;

/**
 * 订单服务接口
 *
 * @author guli
 */
public interface IOrderService {

    /**
     * 创建订单
     *
     * @return 订单
     */
    OmsOrder createOrder();

}
