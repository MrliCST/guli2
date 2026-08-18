package com.atlearn.guli.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;

import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;
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

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public List<SkuEsModel> esearch(ESearchParam param) {
        /*
            bool: 多个查询条件组合
            must: 必须满足的多个条件AND关系 (参与评分计算)
            match: 模糊匹配
            term : 精准查询
            terms : 多次精准查询
            filter: 过滤条件 (不参与评分计算)

            agg -> terms : 精确聚合分桶 (相当于group by `field`)

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
            },
            "agg" : {
                "brand_agg" : {
                    "terms" : {
                        "field" : "brandId",
                        "size" : 10
                    },
                    "agg" : {
                        "brand_name_agg" : {
                            "terms" : {
                                "field" : "brandName",   // 在一个brandId桶中，再次根据 brandName 分桶。实际上，一个id对应一个name，只能分出一个桶来
                                "size" : 10
                            }
                        }
                    }
                },
                "category_agg" : {
                    "terms" : {
                        "field" : "catalogId",
                        "size" : 10
                    },
                    "agg" : {
                        "category_name_agg" : {
                            "terms" : {
                                "field" : "catalogName",  // 还是id对name一对一，但是我们可以看到name了，而不是晦涩的 id
                                "size" : 10
                            }
                        }
                    }
                },
                "attr_agg" : {
                    "nested" : {
                        path : "attrs"
                    },
                    "aggs" : {
                        "attr_id_agg" : {
                            "terms" : {
                                "field" : "attrs.attrId",
                                "size" : 10
                            },
                            "aggs" : {
                                "attr_name_agg" : {
                                    "terms" : {
                                        "field" : "attrs.attrName",
                                        "size" : 10
                                    }
                                },
                                "attr_value_agg" : {
                                    "terms" : {
                                        "field" : "attrs.attrValue",
                                        "size" : 10
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sort : [],
            from : 0,
            size : 5
            highlight : {
                "fields" : {
                    "skuTitle" : {}
                }
                pre_tags : "<b style='color:red'>",
                post_tags : "</b>"
            }
        */

        SearchRequest request = buildRequest(param);

        SearchResponse<SkuEsModel> response;
        try {
            response = elasticsearchClient.search(request, SkuEsModel.class);
        } catch (IOException e) {
            log.error("ES商品检索失败", e);
            throw new RuntimeException("ES商品检索失败", e);
        }

        return parseHits(response);
    }

    /**
     * 构建 SearchRequest：查询 + 聚合 + 高亮 + 排序 + 分页
     */
    private SearchRequest buildRequest(ESearchParam param) {
        // 1. 查询体
        BoolQuery.Builder bool = new BoolQuery.Builder();

        // 1a. 全文检索关键词
        if (param.getKeyword() != null && !param.getKeyword().isBlank()) {
            bool.must(m -> m.match(mt -> mt.field("skuTitle").query(param.getKeyword())));
        }

        // 1b. 筛选条件
        ESearchParam.Filter filter = param.getFilter();
        if (filter != null) {
            // 分类过滤
            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                bool.filter(f -> f.term(t -> t.field("catalogId").value(Long.valueOf(filter.getCategory()))));
            }
            // 是否只查询有货商品
            if (filter.getHasStock() != null) {
                bool.filter(f -> f.term(t -> t.field("hasStock").value(filter.getHasStock())));
            }
            // 价格区间
            ESearchParam.SkuPriceRange skuPrice = filter.getSkuPrice();
            if (skuPrice != null) {
                if (skuPrice.getMin() != null && !skuPrice.getMin().isBlank()) {
                    bool.filter(f -> f.range(r -> r.field("skuPrice").gte(JsonData.of(new BigDecimal(skuPrice.getMin())))));
                }
                if (skuPrice.getMax() != null && !skuPrice.getMax().isBlank()) {
                    bool.filter(f -> f.range(r -> r.field("skuPrice").lte(JsonData.of(new BigDecimal(skuPrice.getMax())))));
                }
            }
            // 品牌过滤
            if (filter.getBrandIds() != null && !filter.getBrandIds().isEmpty()) {
                bool.filter(f -> f.terms(t -> t.field("brandId")
                    .terms(tf -> tf.value(filter.getBrandIds().stream().map(FieldValue::of).toList()))));
            }
            // 属性筛选（nested 嵌套文档）
            Map<String, List<String>> attributes = filter.getAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                attributes.forEach((attrName, attrValues) ->
                    bool.filter(f -> f.nested(n -> n.path("attrs")
                        .query(nq -> nq.bool(nb -> nb
                            .must(nm -> nm.term(t -> t.field("attrs.attrName").value(attrName)))
                            .must(nm -> nm.terms(t -> t.field("attrs.attrValue")
                                .terms(tf -> tf.value(attrValues.stream().map(FieldValue::of).toList()))))
                        )))));
            }
        }

        SearchRequest.Builder builder = new SearchRequest.Builder()
            .index("sku_index")
            .query(q -> q.bool(bool.build()))
            .highlight(h -> h.fields("skuTitle", hf -> hf
                .preTags(List.of("<b style='color:red'>"))
                .postTags(List.of("</b>"))))
            .from(0)
            .size(5);

        // 2. 聚合：品牌 / 分类 / 属性(nested)
        builder.aggregations("brand_agg", Aggregation.of(a -> a.terms(t -> t.field("brandId").size(10))
            .aggregations(Map.of("brand_name_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("brandName").size(10)))))));

        builder.aggregations("category_agg", Aggregation.of(a -> a.terms(t -> t.field("catalogId").size(10))
            .aggregations(Map.of("category_name_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("catalogName").size(10)))))));

        builder.aggregations("attr_agg", Aggregation.of(a -> a.nested(n -> n.path("attrs"))
            .aggregations(Map.of("attr_id_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("attrs.attrId").size(10))
                    .aggregations(Map.of(
                        "attr_name_agg", Aggregation.of(na -> na.terms(t -> t.field("attrs.attrName").size(10))),
                        "attr_value_agg", Aggregation.of(va -> va.terms(t -> t.field("attrs.attrValue").size(10)))
                    )))))));

        // 3. 排序
        if (param.getSortList() != null && !param.getSortList().isEmpty()) {
            List<SortOptions> sorts = new ArrayList<>();
            for (ESearchParam.SortInfo sort : param.getSortList()) {
                SortOptions sortOption = buildSort(sort);
                if (sortOption != null) {
                    sorts.add(sortOption);
                }
            }
            if (!sorts.isEmpty()) {
                builder.sort(sorts);
            }
        }

        return builder.build();
    }

    /**
     * 按排序单元构建排序项
     * sortTag: hot / sales / price；sortOrder: asc / desc
     */
    private SortOptions buildSort(ESearchParam.SortInfo sort) {
        String sortTag = sort.getSortTag();
        if (sortTag == null) {
            return null;
        }
        String field = switch (sortTag) {
            case "hot" -> "hotScore";
            case "sales" -> "saleCount";
            case "price" -> "skuPrice";
            default -> null;
        };
        if (field == null) {
            return null;
        }
        SortOrder order = "asc".equalsIgnoreCase(sort.getSortOrder()) ? SortOrder.Asc : SortOrder.Desc;
        return SortOptions.of(so -> so.field(f -> f.field(field).order(order)));
    }

    /**
     * 将命中文档映射为商品列表，并应用 skuTitle 高亮
     */
    private List<SkuEsModel> parseHits(SearchResponse<SkuEsModel> response) {
        List<SkuEsModel> products = new ArrayList<>();
        if (response.hits() == null || response.hits().hits() == null) {
            return products;
        }
        for (Hit<SkuEsModel> hit : response.hits().hits()) {
            SkuEsModel sku = hit.source();
            if (sku == null) {
                continue;
            }
            // @IndexId 的 skuId 作为 ES 的 _id 存储，_source 中可能没有该字段，需从 _id 回填
            if (sku.getSkuId() == null && hit.id() != null) {
                sku.setSkuId(Long.valueOf(hit.id()));
            }
            // 高亮：命中词被 <b> 包裹
            Map<String, List<String>> highlights = hit.highlight();
            if (highlights != null) {
                List<String> titleHighlights = highlights.get("skuTitle");
                if (titleHighlights != null && !titleHighlights.isEmpty()) {
                    sku.setSkuTitle(titleHighlights.get(0));
                }
            }
            products.add(sku);
        }

        // TODO 聚合结果解析：response.aggregations() 中含 brand_agg / category_agg / attr_agg，
        //  需扩展返回类型（如 SearchResultVo 携带 facets）后在此解析回填。
        return products;
    }
}
