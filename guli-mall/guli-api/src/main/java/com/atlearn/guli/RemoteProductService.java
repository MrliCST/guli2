package com.atlearn.guli;

import java.util.List;

import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVO;

/**
 * 远程商品服务
 * RemoteProductService
 */
public interface RemoteProductService {
    List<RmeSkuSaleAttrValueVO> getSkuSaleAttrValueListBySkuId(Long skuId);
}
