package com.atlearn.guli.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.stereotype.Service;

import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;
import com.atlearn.guli.esmapper.SkuEsMapper;
import com.atlearn.guli.service.IPmsESearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ES商品检索Service业务层处理
 *
 * @author mayao
 * @date 2026-08-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsESearchServiceImpl implements IPmsESearchService {

    private final SkuEsMapper skuEsMapper;

    @Override
    public List<SkuEsModel> esearch(ESearchParam param) {
        LambdaEsQueryWrapper<SkuEsModel> wrapper = new LambdaEsQueryWrapper<>();

        /*
            bool: 多个查询条件组合
            must: 必须满足的条件 (参与评分计算)
            match: 模糊匹配
            term : 精准查询
            filter: 过滤条件 (不参与评分计算)

            query : {
                bool : {
                    must : [
                        {
                            match : {
                                "skuTitle": param.keyword
                            }
                        }
                    ],
                    filter : [
                        {
                            term : {
                                "catalogId" : param.category
                            },
                        },{
                            terms : {
                                "brandId" : [param.brandIds]
                            }
                        },{
                            range : {
                                "skuPrice" : {
                                    "gte" : param.skuPrice.min,
                                    "lte" : param.skuPrice.max
                                }
                            }
                        },{
                            "term": {
                                "hasStock" : param.hasStock
                            }
                        },{
                            "nested": {
                                path : "attrs",
                                query : {
                                    "bool": {
                                        "must": [
                                            {
                                                "term": {
                                                    "attrs.attrName": {
                                                        "value": "屏幕"
                                                    }
                                                },
                                            },{
                                                "terms": {
                                                    "attrs.attrValue": ["6.1寸", "6.5寸"]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        },{
                            "nested": {
                                path : "attrs",
                                query : {
                                    "bool": {
                                        "must": [
                                            {
                                                "term": {
                                                    "attrs.attrName": {
                                                        "value": "颜色"
                                                    }
                                                },
                                            },{
                                                "terms": {
                                                    "attrs.attrValue": ["黑色", "白色"]
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        },
                        ...
                    ]
                }
            }

        */

        // 1. 全文检索关键词
        if (param.getKeyword() != null && !param.getKeyword().isBlank()) {
            wrapper.match(x -> x.getSkuTitle(), param.getKeyword());
        }

        // 2. 筛选条件
        ESearchParam.Filter filter = param.getFilter();
        if (filter != null) {
            // 2a. 分类过滤
            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                wrapper.eq(x -> x.getCatalogId(), Long.valueOf(filter.getCategory()));
            }
            // 2b. 是否只查询有货商品
            if (filter.getHasStock() != null) {
                wrapper.eq(x -> x.getHasStock(), filter.getHasStock());
            }
            // 2c. 价格区间
            ESearchParam.SkuPriceRange skuPrice = filter.getSkuPrice();
            if (skuPrice != null) {
                if (skuPrice.getMin() != null && !skuPrice.getMin().isBlank()) {
                    wrapper.ge(x -> x.getSkuPrice(), new BigDecimal(skuPrice.getMin()));
                }
                if (skuPrice.getMax() != null && !skuPrice.getMax().isBlank()) {
                    wrapper.le(x -> x.getSkuPrice(), new BigDecimal(skuPrice.getMax()));
                }
            }
            // 2d. 品牌过滤
            if (filter.getBrandIds() != null && !filter.getBrandIds().isEmpty()) {
                wrapper.in(x -> x.getBrandId(), filter.getBrandIds());
            }
            // 2e. 属性筛选（attrs 为 nested 嵌套文档，需用 nested 查询）
            Map<String, List<String>> attributes = filter.getAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                attributes.forEach((attrName, attrValues) ->
                    wrapper.nested("attrs", w -> w
                        .eq("attrName", attrName)
                        .in("attrValue", attrValues)));
            }
        }

        // 3. 排序
        if (param.getSortList() != null) {
            param.getSortList().forEach(sort -> applySort(wrapper, sort));
        }

        return skuEsMapper.selectList(wrapper);
    }

    /**
     * 按排序单元设置排序规则
     * sortTag: hot / sales / price；sortOrder: asc / desc
     */
    private void applySort(LambdaEsQueryWrapper<SkuEsModel> wrapper, ESearchParam.SortInfo sort) {
        String sortTag = sort.getSortTag();
        if (sortTag == null) {
            return;
        }
        boolean asc = "asc".equalsIgnoreCase(sort.getSortOrder());
        switch (sortTag) {
            case "hot":
                if (asc) {
                    wrapper.orderByAsc(x -> x.getHotScore());
                } else {
                    wrapper.orderByDesc(x -> x.getHotScore());
                }
                break;
            case "sales":
                if (asc) {
                    wrapper.orderByAsc(x -> x.getSaleCount());
                } else {
                    wrapper.orderByDesc(x -> x.getSaleCount());
                }
                break;
            case "price":
                if (asc) {
                    wrapper.orderByAsc(x -> x.getSkuPrice());
                } else {
                    wrapper.orderByDesc(x -> x.getSkuPrice());
                }
                break;
            default:
                break;
        }
    }
}
