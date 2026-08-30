package com.atlearn.guli;

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

}
