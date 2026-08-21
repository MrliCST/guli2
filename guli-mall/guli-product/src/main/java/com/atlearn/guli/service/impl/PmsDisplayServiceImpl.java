package com.atlearn.guli.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;

import com.atlearn.guli.config.GuliEsConstant;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.domain.vo.PmsSkuItemVo;
import com.atlearn.guli.dto.ESearchListVo;
import com.atlearn.guli.dto.ESearchParam;
import com.atlearn.guli.dto.SkuEsModel;
import com.atlearn.guli.dto.ESearchListVo.AttrInfo;
import com.atlearn.guli.dto.ESearchListVo.BrandInfo;
import com.atlearn.guli.dto.ESearchListVo.CategoryInfo;
import com.atlearn.guli.domain.PmsSkuImages;
import com.atlearn.guli.domain.PmsSkuInfo;
import com.atlearn.guli.domain.PmsSpuInfoDesc;
import com.atlearn.guli.mapper.PmsProductAttrValueMapper;
import com.atlearn.guli.mapper.PmsSkuImagesMapper;
import com.atlearn.guli.mapper.PmsSkuInfoMapper;
import com.atlearn.guli.mapper.PmsSkuSaleAttrValueMapper;
import com.atlearn.guli.mapper.PmsSpuInfoDescMapper;
import com.atlearn.guli.service.IPmsDisplayService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品展示Service业务层处理
 * 1. ES商品检索
 * 2. sku详情查询
 *
 * @author mayao
 * @date 2026-08-17
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsDisplayServiceImpl implements IPmsDisplayService {

    private final ElasticsearchClient elasticsearchClient;

    // sku信息查询
    private final PmsSkuInfoMapper baseMapper;
    // sku图片查询
    private final PmsSkuImagesMapper skuImagesMapper;
    // sku销售属性查询
    private final PmsSkuSaleAttrValueMapper skuSaleAttrValueMapper;
    // spu属性值查询
    private final PmsProductAttrValueMapper productAttrValueMapper;
    // spu介绍查询
    private final PmsSpuInfoDescMapper spuInfoDescMapper;
    // 异步线程池（商品详情页多源查询并行）
    @Qualifier("displayExecutor")
    private final Executor displayExecutor;

    // ==================== 索引与字段常量 ====================
    private static final String INDEX_NAME = "sku_index";

    // 文档字段
    private static final String FIELD_SKU_TITLE = "skuTitle";
    private static final String FIELD_CATALOG_ID = "catalogId";
    private static final String FIELD_CATALOG_NAME = "catalogName";
    private static final String FIELD_HAS_STOCK = "hasStock";
    private static final String FIELD_SKU_PRICE = "skuPrice";
    private static final String FIELD_BRAND_ID = "brandId";
    private static final String FIELD_BRAND_NAME = "brandName";
    private static final String FIELD_BRAND_IMG = "brandImg";
    private static final String FIELD_HOT_SCORE = "hotScore";
    private static final String FIELD_SALE_COUNT = "saleCount";

    // nested 路径与子字段
    private static final String NESTED_PATH_ATTRS = "attrs";
    private static final String FIELD_ATTR_ID = "attrs.attrId";
    private static final String FIELD_ATTR_NAME = "attrs.attrName";
    private static final String FIELD_ATTR_VALUE = "attrs.attrValue";

    // ==================== 聚合名称常量 ====================
    private static final String AGG_BRAND = "brand_agg";
    private static final String AGG_BRAND_NAME = "brand_name_agg";
    private static final String AGG_BRAND_IMG = "brand_img_agg";
    private static final String AGG_CATEGORY = "category_agg";
    private static final String AGG_CATEGORY_NAME = "category_name_agg";
    private static final String AGG_ATTR = "attr_agg";
    private static final String AGG_ATTR_ID = "attr_id_agg";
    private static final String AGG_ATTR_NAME = "attr_name_agg";
    private static final String AGG_ATTR_VALUE = "attr_value_agg";

    // 聚合分桶大小
    private static final int AGG_BUCKET_SIZE = 10;

    // ==================== 排序与高亮常量 ====================
    private static final String SORT_TAG_HOT = "hot";
    private static final String SORT_TAG_SALES = "sales";
    private static final String SORT_TAG_PRICE = "price";
    private static final String SORT_ORDER_ASC = "asc";

    private static final String HL_PRE_TAG = "<b style='color:red'>";
    private static final String HL_POST_TAG = "</b>";

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
            bool.must(m -> m.match(mt -> mt.field(FIELD_SKU_TITLE).query(param.getKeyword())));
        }

        // 1b. 筛选条件
        ESearchParam.Filter filter = param.getFilter();
        if (filter != null) {
            // 分类过滤
            if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                bool.filter(f -> f.term(t -> t.field(FIELD_CATALOG_ID).value(Long.valueOf(filter.getCategory()))));
            }
            // 是否只查询有货商品
            if (filter.getHasStock() != null) {
                bool.filter(f -> f.term(t -> t.field(FIELD_HAS_STOCK).value(filter.getHasStock())));
            }
            // 价格区间
            ESearchParam.SkuPriceRange skuPrice = filter.getSkuPrice();
            if (skuPrice != null) {
                if (skuPrice.getMin() != null && !skuPrice.getMin().isBlank()) {
                    bool.filter(f -> f.range(r -> r.field(FIELD_SKU_PRICE).gte(JsonData.of(new BigDecimal(skuPrice.getMin())))));
                }
                if (skuPrice.getMax() != null && !skuPrice.getMax().isBlank()) {
                    bool.filter(f -> f.range(r -> r.field(FIELD_SKU_PRICE).lte(JsonData.of(new BigDecimal(skuPrice.getMax())))));
                }
            }
            // 品牌过滤
            if (filter.getBrandIds() != null && !filter.getBrandIds().isEmpty()) {
                bool.filter(f -> f.terms(t -> t.field(FIELD_BRAND_ID)
                    .terms(tf -> tf.value(filter.getBrandIds().stream().map(FieldValue::of).toList()))));
            }
            // 属性筛选（nested 嵌套文档）
            Map<String, List<String>> attributes = filter.getAttributes();
            if (attributes != null && !attributes.isEmpty()) {
                attributes.forEach((attrName, attrValues) ->
                    bool.filter(f -> f.nested(n -> n.path(NESTED_PATH_ATTRS)
                        .query(nq -> nq.bool(nb -> nb
                            .must(nm -> nm.term(t -> t.field(FIELD_ATTR_NAME).value(attrName)))
                            .must(nm -> nm.terms(t -> t.field(FIELD_ATTR_VALUE)
                                .terms(tf -> tf.value(attrValues.stream().map(FieldValue::of).toList()))))
                        )))
                    )
                );
            }
        }

        SearchRequest.Builder builder = new SearchRequest.Builder()
            .index(INDEX_NAME)
            .query(q -> q.bool(bool.build()))
            .highlight(h -> h.fields(FIELD_SKU_TITLE, hf -> hf
                .preTags(List.of(HL_PRE_TAG))
                .postTags(List.of(HL_POST_TAG))))
            .from(0)
            .size(GuliEsConstant.pageNum);

        // 2. 聚合：品牌 / 分类 / 属性(nested)
        builder.aggregations(AGG_BRAND, Aggregation.of(a -> a.terms(t -> t.field(FIELD_BRAND_ID).size(AGG_BUCKET_SIZE))
            .aggregations(Map.of(
                AGG_BRAND_NAME,
                Aggregation.of(sa -> sa.terms(t -> t.field(FIELD_BRAND_NAME).size(AGG_BUCKET_SIZE))),
                AGG_BRAND_IMG,
                Aggregation.of(sa -> sa.terms(t -> t.field(FIELD_BRAND_IMG).size(AGG_BUCKET_SIZE))))
            )
        ));

        builder.aggregations(AGG_CATEGORY, Aggregation.of(a -> a.terms(t -> t.field(FIELD_CATALOG_ID).size(AGG_BUCKET_SIZE))
            .aggregations(Map.of(
                AGG_CATEGORY_NAME,
                Aggregation.of(sa -> sa.terms(t -> t.field(FIELD_CATALOG_NAME).size(AGG_BUCKET_SIZE))))
            )
        ));

        builder.aggregations(AGG_ATTR, Aggregation.of(a -> a.nested(n -> n.path(NESTED_PATH_ATTRS))
            .aggregations(Map.of(
                AGG_ATTR_ID,
                Aggregation.of(sa -> sa.terms(t -> t.field(FIELD_ATTR_ID).size(AGG_BUCKET_SIZE))
                    .aggregations(Map.of(
                        AGG_ATTR_NAME,
                        Aggregation.of(na -> na.terms(t -> t.field(FIELD_ATTR_NAME).size(AGG_BUCKET_SIZE))),
                        AGG_ATTR_VALUE,
                        Aggregation.of(va -> va.terms(t -> t.field(FIELD_ATTR_VALUE).size(AGG_BUCKET_SIZE)))
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
            case SORT_TAG_HOT -> FIELD_HOT_SCORE;
            case SORT_TAG_SALES -> FIELD_SALE_COUNT;
            case SORT_TAG_PRICE -> FIELD_SKU_PRICE;
            default -> null;
        };
        if (field == null) {
            return null;
        }
        SortOrder order = SORT_ORDER_ASC.equalsIgnoreCase(sort.getSortOrder()) ? SortOrder.Asc : SortOrder.Desc;
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
        if (total != null) {
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

        Map<String, Aggregate> aggregations = response.aggregations();

        // 获取品牌的聚合
        List<BrandInfo> brandInfos = aggregations.get(AGG_BRAND)
            .sterms()  // 查询使用 terms 聚合，那么解析也使用 terms
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long brandId = bucket.key().longValue(); // 取键对应的值
                String brandName = bucket.aggregations().get(AGG_BRAND_NAME)
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                String brandImg = bucket.aggregations().get(AGG_BRAND_IMG)
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                BrandInfo brandInfo = new BrandInfo();
                brandInfo.setBrandId(brandId);
                brandInfo.setBrandName(brandName);
                brandInfo.setBrandImg(brandImg);
                return brandInfo;
            }).collect(Collectors.toList());
        esListVo.setBrands(brandInfos);

        // 获取分类的聚合
        List<CategoryInfo> categoryInfos = aggregations.get(AGG_CATEGORY)
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long catId = bucket.key().longValue();
                String catName = bucket.aggregations().get(AGG_CATEGORY_NAME)
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();
                CategoryInfo categoryInfo = new CategoryInfo();
                categoryInfo.setCategoryId(catId);
                categoryInfo.setCategoryName(catName);
                return categoryInfo;
            }).collect(Collectors.toList());
        esListVo.setCategories(categoryInfos);

        // 获取属性的聚合（nested -> attr_id_agg）
        List<AttrInfo> attrInfos = aggregations.get(AGG_ATTR).nested().aggregations().get(AGG_ATTR_ID)
            .sterms()
            .buckets()
            .array()
            .stream()
            .map(bucket -> {
                long attrId = bucket.key().longValue();
                String attrName = bucket.aggregations().get(AGG_ATTR_NAME)
                    .sterms()
                    .buckets()
                    .array().get(0)
                    .key()
                    .stringValue();

                List<String> attrValues = bucket.aggregations().get(AGG_ATTR_VALUE)
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(b -> b.key().stringValue())
                    .collect(Collectors.toList());

                AttrInfo attrInfo = new AttrInfo();
                attrInfo.setAttrId(attrId);
                attrInfo.setAttrName(attrName);
                attrInfo.setAttrValue(attrValues);
                return attrInfo;
            }).collect(Collectors.toList());
        esListVo.setAttrs(attrInfos);

        return esListVo;
    }

    /**
     * sku详情查询（异步编排）
     *
     * @param skuId skuId
     * @return sku详情
     */
    @Override
    public PmsSkuItemVo item(Long skuId) {
        PmsSkuItemVo vo = new PmsSkuItemVo();

        // 1. 首次 并行查询 skuInfo 和 baseAttr
        CompletableFuture<PmsSkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            PmsSkuInfo skuInfo = baseMapper.selectById(skuId);
            vo.setSkuInfo(skuInfo);
            return skuInfo;
        }, displayExecutor);

        CompletableFuture<Void> skuImagesFuture = CompletableFuture.runAsync(() -> {
            List<PmsSkuImages> skuImages = skuImagesMapper.selectList(
                Wrappers.<PmsSkuImages>lambdaQuery().eq(x -> x.getSkuId(), skuId));
            vo.setSkuImages(skuImages);
        }, displayExecutor);

        // 2. 继 skuInfo 完成后 并行查询 spuInfoDesc、saleAttrs、baseAttrGroups
        CompletableFuture<Void> baseAttrGroupsFutureAsync1 = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            List<PmsSkuItemVo.SpuBaseAttrGroup> baseAttrGroups = productAttrValueMapper.selectBaseAttrGroupBySpuId(
                skuInfo.getSpuId(), skuInfo.getCatalogId());
            vo.setSpuBaseAttrGroup(baseAttrGroups);
        }, displayExecutor);

        CompletableFuture<Void> SpuInfoDescFutureAsync2 = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            PmsSpuInfoDesc spuInfoDesc = spuInfoDescMapper.selectById(skuInfo.getSpuId());
            vo.setSpuInfoDesc(spuInfoDesc);
        }, displayExecutor);

        CompletableFuture<Void> saleAttrsFutureAsync3 = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            List<PmsSkuItemVo.SkuItemSaleAttr> saleAttrs = skuSaleAttrValueMapper.selectSaleAttrsBySpuId(skuInfo.getSpuId());
            vo.setSkuItemSaleAttr(saleAttrs);
        }, displayExecutor);

        try {
            CompletableFuture.allOf(skuImagesFuture, baseAttrGroupsFutureAsync1, SpuInfoDescFutureAsync2, saleAttrsFutureAsync3)
                .join();
        } catch (CompletionException e) {
            log.error("异步编排异常：{}", e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.ASYNC_READ_SKUITEM_FAILED, e);
        }

        return vo;
    }
}
