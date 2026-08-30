package com.atlearn.guli;

import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVo;

import java.util.List;

/**
 * 远程商品服务
 * RemoteProductService
 */
public interface RemoteProductService {

    /**
     * 根据 skuId 获取 saleAtrr 信息
     * @param skuId
     * @return
     */
    List<RmeSkuSaleAttrValueVo> getSkuSaleAttrValueListBySkuId(Long skuId);

    /**
     * 根据 skuId 获取 SKU 信息
     *
     * @param skuId skuId
     * @return SKU 信息
     */
    RmeSkuInfoVo getSkuInfoBySkuId(Long skuId);
}
