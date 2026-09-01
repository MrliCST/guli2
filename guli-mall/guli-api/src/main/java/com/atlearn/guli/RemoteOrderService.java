package com.atlearn.guli;

/**
 * 订单远程服务接口
 *
 * @author guli
 */
public interface RemoteOrderService {

    /**
     * 根据订单号查询订单状态
     * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
     *
     * @param orderSn 订单号
     * @return 订单状态，null=订单不存在
     */
    Integer getOrderStatusBySn(String orderSn);
}
