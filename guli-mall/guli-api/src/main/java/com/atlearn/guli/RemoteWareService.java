package com.atlearn.guli;

import com.atlearn.guli.domain.bo.RmeWareSkuLockBo;
import com.atlearn.guli.domain.vo.RmeWareStockLockResultVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RemoteWareService {

    /**
     * 检查sku是否有对应的库存
     * @param skuIds
     * @return map[skuId] => skuAvailableStock
     */
    Map<Long,Long> getSkuAvailableStock(List<Long> skuIds);

    /**
     * 模拟获取运费
     * @return
     */
    BigDecimal getShippingFee(String ReceiverAddressInfo);

    /**
     * 订单锁库存
     *
     * @param lockBo 锁定请求（订单号 + 商品列表）
     * @return 锁定结果
     */
    RmeWareStockLockResultVo orderLockStock(RmeWareSkuLockBo lockBo);

}
