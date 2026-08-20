package com.atlearn.guli.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;

import com.atlearn.guli.config.GuliEsConstant;
import com.atlearn.guli.dto.ESearchListVo;
import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;
import com.atlearn.guli.dto.ESearchListVo.AttrInfo;
import com.atlearn.guli.dto.ESearchListVo.BrandInfo;
import com.atlearn.guli.dto.ESearchListVo.CategoryInfo;
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

    /**
     * @param param 检索参数
     * @return 商品检索结果列表
     */
    @Override
    public ESearchListVo esearch(ESearchParam param) {
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
                                "field" : "brandName",  
                                "size" : 10
                            }
                        },
                        "brand_img_agg" : {
                            "terms" : {
                                "field" : "brandImg",  
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
                                "field" : "catalogName",  
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
            .size(GuliEsConstant.pageNum);

        // 2. 聚合：品牌 / 分类 / 属性(nested)
        builder.aggregations("brand_agg", Aggregation.of(a -> a.terms(t -> t.field("brandId").size(10))
            .aggregations(Map.of(
                "brand_name_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("brandName").size(10))),
                "brand_img_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("brandImg").size(10))))
            )
        ));

        builder.aggregations("category_agg", Aggregation.of(a -> a.terms(t -> t.field("catalogId").size(10))
            .aggregations(Map.of(
                "category_name_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("catalogName").size(10))))
            )
        ));

        builder.aggregations("attr_agg", Aggregation.of(a -> a.nested(n -> n.path("attrs"))
            .aggregations(Map.of(
                "attr_id_agg",
                Aggregation.of(sa -> sa.terms(t -> t.field("attrs.attrId").size(10))
                    .aggregations(Map.of(
                        "attr_name_agg", 
                        Aggregation.of(na -> na.terms(t -> t.field("attrs.attrName").size(10))),
                        "attr_value_agg", 
                        Aggregation.of(va -> va.terms(t -> t.field("attrs.attrValue").size(10)))
                    ))
                )
            ))
        ));

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
     * 将命中的 hits，封装为 ESearchListVo
     */
    private ESearchListVo parseHits(SearchResponse<SkuEsModel> response) {
        ESearchListVo esListVo = new ESearchListVo();
        
        // hits -> The returned documents and metadata.
        HitsMetadata<SkuEsModel> docMetaHits = response.hits();
        TotalHits total = docMetaHits.total();

        // 获取总命中数, 分页大小
        Long totalHits = null;
        if(total != null){
            totalHits = total.value();
        }
        esListVo.setTotal(totalHits);
        esListVo.setPageSize(GuliEsConstant.pageNum);

        // 获取命中的 product
        List<Hit<SkuEsModel>> modelHits = docMetaHits.hits();
        List<SkuEsModel> products = modelHits.stream()
            .map(hit -> hit.source())
            .collect(Collectors.toList());
        esListVo.setProducts(products);

        Map<String,Aggregate> aggregations = response.aggregations();

        // 获取品牌的聚合 
        List<ESearchListVo.BrandInfo> brandInfos = aggregations.get("brand_agg")
            .sterms()  // 查询使用 terms 聚合，那么解析也使用 terms
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long brandId = bucket.key().longValue(); // 取键对应的值
                String brandName = bucket.aggregations().get("brand_name_agg")
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                String brandImg = bucket.aggregations().get("brand_img_agg")
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                ESearchListVo.BrandInfo brandInfo = new ESearchListVo.BrandInfo();
                brandInfo.setBrandId(brandId);
                brandInfo.setBrandName(brandName);
                brandInfo.setBrandImg(brandImg);
                return brandInfo;
            }).collect(Collectors.toList());
        esListVo.setBrands(brandInfos);

        List<CategoryInfo> categoryInfos = aggregations.get("category_agg")
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long catId = bucket.key().longValue();
                String catName = bucket.aggregations().get("cat_name_agg")
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                ESearchListVo.CategoryInfo categoryInfo = new ESearchListVo.CategoryInfo();
                categoryInfo.setCategoryId(catId);
                categoryInfo.setCategoryName(catName);
                return categoryInfo;
            }).collect(Collectors.toList());
        esListVo.setCategories(categoryInfos);

        List<AttrInfo> attrInfos = aggregations.get("attr_agg").nested().aggregations().get("att_id_agg")
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long attrId = bucket.key().longValue();
                String attrName = bucket.aggregations().get("attr_name_agg")
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();

                List<String> attrValues = bucket.aggregations().get("attr_value_agg")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(b -> b.key().stringValue())
                    .collect(Collectors.toList());
                
                ESearchListVo.AttrInfo attrInfo = new ESearchListVo.AttrInfo();
                attrInfo.setAttrId(attrId);
                attrInfo.setAttrName(attrName);
                attrInfo.setAttrValue(attrValues);
                return attrInfo;
            }).collect(Collectors.toList());
        esListVo.setAttrs(attrInfos);
        
        return esListVo;
    }
}
