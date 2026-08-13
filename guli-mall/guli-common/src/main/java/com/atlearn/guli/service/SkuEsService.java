package com.atlearn.guli.service;

import com.atlearn.guli.dto.SkuEsModel;
import com.atlearn.guli.esmapper.SkuEsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.kernel.EsWrappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SKU 检索服务（Easy-Es）
 *
 * @author mayao
 * @date 2026-08-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
public class SkuEsService {

    private final SkuEsMapper skuEsMapper;

    /**
     * 索引单个 SKU
     */
    public void index(SkuEsModel model) {
        skuEsMapper.insert(model);
    }

    /**
     * 批量索引 SKU
     */
    public void indexBatch(List<SkuEsModel> list) {
        skuEsMapper.insertBatch(list);
    }

    /**
     * 按 spuId 删除（重新上架前先清旧数据）
     */
    public void deleteBySpuId(Long spuId) {
        skuEsMapper.delete(EsWrappers.lambdaQuery(SkuEsModel.class).eq(x -> x.getSpuId(), spuId));
    }

    /**
     * 检索（关键词 + 分类 + 品牌，热度倒序，分页）
     */
    public EsPageInfo<SkuEsModel> search(String keyword, Long catalogId, Long brandId, Integer pageNum, Integer pageSize) {
        LambdaEsQueryWrapper<SkuEsModel> wrapper = EsWrappers.lambdaQuery(SkuEsModel.class);
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.match(x -> x.getSkuTitle(), keyword);
        }
        if (catalogId != null) {
            wrapper.eq(x -> x.getCatalogId(), catalogId);
        }
        if (brandId != null) {
            wrapper.eq(SkuEsModel::getBrandId, brandId);
        }
        wrapper.orderByDesc(SkuEsModel::getHotScore);
        return skuEsMapper.pageQuery(wrapper, pageNum, pageSize);
    }
}
