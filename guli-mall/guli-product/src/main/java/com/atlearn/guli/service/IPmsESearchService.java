package com.atlearn.guli.service;

import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;

import java.util.List;

/**
 * ES商品检索Service接口
 *
 * @author mayao
 * @date 2026-08-17
 */
public interface IPmsESearchService {

    /**
     * 商品检索
     *
     * @param param 检索参数
     * @return 商品检索结果列表
     */
    List<SkuEsModel> esearch(ESearchParam param);
}
