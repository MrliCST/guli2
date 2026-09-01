package com.atlearn.guli;

import com.atlearn.guli.domain.bo.RmeLockWareBo;
import com.atlearn.guli.domain.bo.RmeOrderInfoBo;
import com.atlearn.guli.domain.vo.RmeWareSkuVo;

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
     * 根据 skuId 列表批量查询各仓库的库存信息（含可用库存），按可用库存降序排列
     *
     * @param skuIds skuId 列表
     * @return 仓库库存列表
     */
    List<RmeWareSkuVo> getWareSkuListBySkuIds(List<Long> skuIds);

    /**
     * 批量CAS锁定仓库库存，同时插入库存工作单及明细
     *
     * @param orderInfo      订单冗余信息（用于工作单主表）
     * @param lockWareBoList 锁定信息
     * @return true=全部锁定成功，失败时抛异常并回滚
     */
    boolean lockWareSkuBatch(RmeOrderInfoBo orderInfo, List<RmeLockWareBo> lockWareBoList);

    /**
     * 模拟获取运费
     * @return
     */
    BigDecimal getShippingFee(String ReceiverAddressInfo);
}
