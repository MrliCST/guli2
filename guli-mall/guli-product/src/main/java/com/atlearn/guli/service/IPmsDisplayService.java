package com.atlearn.guli.service;

import com.atlearn.guli.domain.vo.PmsSkuItemVo;
import com.atlearn.guli.domain.ESearchListVo;
import com.atlearn.guli.domain.ESearchParam;

/**
 * 商品展示Service接口
 * 1. ES商品检索
 * 2. sku详情查询
 *
 * @author mayao
 * @date 2026-08-17
 */
public interface IPmsDisplayService {

    /**
     * 商品检索
     *
     * @param param 检索参数
     * @return 商品检索结果列表
     */
    ESearchListVo esearch(ESearchParam param);

    /**
     * sku详情查询
     *
     * @param skuId skuId
     * @return sku详情
     */
    PmsSkuItemVo item(Long skuId);
}
