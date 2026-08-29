package com.atlearn.guli.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;

import com.atlearn.guli.constant.GuliEsConstant;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import com.atlearn.guli.domain.vo.PmsSkuItemVo;
import com.atlearn.guli.domain.ESearchListVo;
import com.atlearn.guli.domain.ESearchListVo.AttrInfo;
import com.atlearn.guli.domain.ESearchListVo.BrandInfo;
import com.atlearn.guli.domain.ESearchListVo.CategoryInfo;
import com.atlearn.guli.domain.ESearchParam;
import com.atlearn.guli.domain.SkuEsModel;
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

    // easy-es 3.x 自动装配的 ES 原生客户端（co.elastic.clients 新 Java API Client）
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

    /**
     * @param param 检索参数
     * @return 商品检索结果列表
     */
    @Override
    public ESearchListVo esearch(ESearchParam param) {
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
        return SearchRequest.of(s -> {
            // 1. 查询体：bool(must=关键词 + filter=各筛选条件)
            s.index(GuliEsConstant.INDEX_NAME)
                .query(q -> q.bool(b -> {
                    // 1a. 全文检索关键词
                    if (param.getKeyword() != null && !param.getKeyword().isBlank()) {
                        b.must(m -> m.match(mt -> mt.field(GuliEsConstant.FIELD_SKU_TITLE).query(param.getKeyword())));
                    }

                    // 1b. 筛选条件
                    ESearchParam.Filter filter = param.getFilter();
                    if (filter != null) {
                        // 分类过滤
                        if (filter.getCategory() != null && !filter.getCategory().isBlank()) {
                            b.filter(f -> f.term(t -> t.field(GuliEsConstant.FIELD_CATALOG_ID).value(Long.valueOf(filter.getCategory()))));
                        }
                        // 是否只查询有货商品
                        if (filter.getHasStock() != null) {
                            b.filter(f -> f.term(t -> t.field(GuliEsConstant.FIELD_HAS_STOCK).value(filter.getHasStock())));
                        }
                        // 价格区间
                        ESearchParam.SkuPriceRange skuPrice = filter.getSkuPrice();
                        if (skuPrice != null) {
                            if (skuPrice.getMin() != null && !skuPrice.getMin().isBlank()) {
                                b.filter(f -> f.range(r -> r.field(GuliEsConstant.FIELD_SKU_PRICE).gte(JsonData.of(new BigDecimal(skuPrice.getMin())))));
                            }
                            if (skuPrice.getMax() != null && !skuPrice.getMax().isBlank()) {
                                b.filter(f -> f.range(r -> r.field(GuliEsConstant.FIELD_SKU_PRICE).lte(JsonData.of(new BigDecimal(skuPrice.getMax())))));
                            }
                        }
                        // 品牌过滤
                        if (filter.getBrandIds() != null && !filter.getBrandIds().isEmpty()) {
                            b.filter(f -> f.terms(t -> t.field(GuliEsConstant.FIELD_BRAND_ID)
                                .terms(tf -> tf.value(filter.getBrandIds().stream().map(FieldValue::of).toList()))));
                        }
                        // 属性筛选（nested 嵌套文档）
                        Map<String, List<String>> attributes = filter.getAttributes();
                        if (attributes != null && !attributes.isEmpty()) {
                            attributes.forEach((attrName, attrValues) ->
                                b.filter(f -> f.nested(n -> n.path(GuliEsConstant.NESTED_PATH_ATTRS)
                                    .query(nq -> nq.bool(nb -> nb
                                        .must(nm -> nm.term(t -> t.field(GuliEsConstant.FIELD_ATTR_NAME).value(attrName)))
                                        .must(nm -> nm.terms(t -> t.field(GuliEsConstant.FIELD_ATTR_VALUE)
                                            .terms(tf -> tf.value(attrValues.stream().map(FieldValue::of).toList()))))
                                    )))
                                )
                            );
                        }
                    }
                    return b;
                }))
                // 2. 高亮
                .highlight(h -> h.fields(GuliEsConstant.FIELD_SKU_TITLE, hf -> hf
                    .preTags(List.of(GuliEsConstant.HL_PRE_TAG))
                    .postTags(List.of(GuliEsConstant.HL_POST_TAG))))
                // 3. 分页
                .from(0)
                .size(GuliEsConstant.PAGE_NUM)
                // 4. 聚合：品牌 / 分类 / 属性(nested)
                .aggregations(GuliEsConstant.AGG_BRAND, a -> a.terms(t -> t.field(GuliEsConstant.FIELD_BRAND_ID).size(GuliEsConstant.AGG_BUCKET_SIZE))
                    .aggregations(GuliEsConstant.AGG_BRAND_NAME, a2 -> a2.terms(t2 -> t2.field(GuliEsConstant.FIELD_BRAND_NAME).size(GuliEsConstant.AGG_BUCKET_SIZE)))
                    .aggregations(GuliEsConstant.AGG_BRAND_IMG, a3 -> a3.terms(t3 -> t3.field(GuliEsConstant.FIELD_BRAND_IMG).size(GuliEsConstant.AGG_BUCKET_SIZE))))
                .aggregations(GuliEsConstant.AGG_CATEGORY, a -> a.terms(t -> t.field(GuliEsConstant.FIELD_CATALOG_ID).size(GuliEsConstant.AGG_BUCKET_SIZE))
                    .aggregations(GuliEsConstant.AGG_CATEGORY_NAME, a2 -> a2.terms(t2 -> t2.field(GuliEsConstant.FIELD_CATALOG_NAME).size(GuliEsConstant.AGG_BUCKET_SIZE))))
                .aggregations(GuliEsConstant.AGG_ATTR, a -> a.nested(n -> n.path(GuliEsConstant.NESTED_PATH_ATTRS))
                    .aggregations(GuliEsConstant.AGG_ATTR_ID, a2 -> a2.terms(t2 -> t2.field(GuliEsConstant.FIELD_ATTR_ID).size(GuliEsConstant.AGG_BUCKET_SIZE))
                        .aggregations(GuliEsConstant.AGG_ATTR_NAME, a3 -> a3.terms(t3 -> t3.field(GuliEsConstant.FIELD_ATTR_NAME).size(GuliEsConstant.AGG_BUCKET_SIZE)))
                        .aggregations(GuliEsConstant.AGG_ATTR_VALUE, a4 -> a4.terms(t4 -> t4.field(GuliEsConstant.FIELD_ATTR_VALUE).size(GuliEsConstant.AGG_BUCKET_SIZE)))));

            // 5. 排序
            if (param.getSortList() != null && !param.getSortList().isEmpty()) {
                for (ESearchParam.SortInfo sort : param.getSortList()) {
                    String field = resolveSortField(sort.getSortTag());
                    if (field != null) {
                        SortOrder order = GuliEsConstant.SORT_ORDER_ASC.equalsIgnoreCase(sort.getSortOrder())
                            ? SortOrder.Asc : SortOrder.Desc;
                        s.sort(so -> so.field(f -> f.field(field).order(order)));
                    }
                }
            }

            return s;
        });
    }

    /**
     * 按排序标签解析排序字段
     * sortTag: hot / sales / price
     */
    private String resolveSortField(String sortTag) {
        if (sortTag == null) {
            return null;
        }
        return switch (sortTag) {
            case GuliEsConstant.SORT_TAG_HOT -> GuliEsConstant.FIELD_HOT_SCORE;
            case GuliEsConstant.SORT_TAG_SALES -> GuliEsConstant.FIELD_SALE_COUNT;
            case GuliEsConstant.SORT_TAG_PRICE -> GuliEsConstant.FIELD_SKU_PRICE;
            default -> null;
        };
    }

    /**
     * 将命中的 hits，封装为 ESearchListVo
     */
    private ESearchListVo parseHits(SearchResponse<SkuEsModel> response) {
        ESearchListVo esListVo = new ESearchListVo();

        // 获取总命中数
        var total = response.hits().total();
        esListVo.setTotal(total == null ? null : total.value());
        esListVo.setPageSize(GuliEsConstant.PAGE_NUM);

        // 获取命中的商品
        List<SkuEsModel> products = new ArrayList<>();
        for (var hit : response.hits().hits()) {
            products.add(hit.source());
        }
        esListVo.setProducts(products);

        // 解析聚合结果
        var aggregations = response.aggregations();
        if (aggregations == null || aggregations.isEmpty()) {
            return esListVo;
        }

        // 品牌聚合
        var brandAgg = aggregations.get(GuliEsConstant.AGG_BRAND);
        if (brandAgg != null) {
            List<BrandInfo> brandInfos = new ArrayList<>();
            for (var bucket : brandAgg.sterms().buckets().array()) {
                BrandInfo brandInfo = new BrandInfo();
                brandInfo.setBrandId(bucket.key().longValue());
                var brandNameBuckets = bucket.aggregations().get(GuliEsConstant.AGG_BRAND_NAME).sterms().buckets().array();
                if (!brandNameBuckets.isEmpty()) {
                    brandInfo.setBrandName(brandNameBuckets.get(0).key().stringValue());
                }
                var brandImgBuckets = bucket.aggregations().get(GuliEsConstant.AGG_BRAND_IMG).sterms().buckets().array();
                if (!brandImgBuckets.isEmpty()) {
                    brandInfo.setBrandImg(brandImgBuckets.get(0).key().stringValue());
                }
                brandInfos.add(brandInfo);
            }
            esListVo.setBrands(brandInfos);
        }

        // 分类聚合
        var categoryAgg = aggregations.get(GuliEsConstant.AGG_CATEGORY);
        if (categoryAgg != null) {
            List<CategoryInfo> categoryInfos = new ArrayList<>();
            for (var bucket : categoryAgg.sterms().buckets().array()) {
                CategoryInfo categoryInfo = new CategoryInfo();
                categoryInfo.setCategoryId(bucket.key().longValue());
                var categoryNameBuckets = bucket.aggregations().get(GuliEsConstant.AGG_CATEGORY_NAME).sterms().buckets().array();
                if (!categoryNameBuckets.isEmpty()) {
                    categoryInfo.setCategoryName(categoryNameBuckets.get(0).key().stringValue());
                }
                categoryInfos.add(categoryInfo);
            }
            esListVo.setCategories(categoryInfos);
        }

        // 属性聚合（nested -> attr_id_agg）
        var attrNested = aggregations.get(GuliEsConstant.AGG_ATTR);
        if (attrNested != null) {
            var attrIdAgg = attrNested.nested().aggregations().get(GuliEsConstant.AGG_ATTR_ID);
            if (attrIdAgg != null) {
                List<AttrInfo> attrInfos = new ArrayList<>();
                for (var bucket : attrIdAgg.sterms().buckets().array()) {
                    AttrInfo attrInfo = new AttrInfo();
                    attrInfo.setAttrId(bucket.key().longValue());
                    var attrNameBuckets = bucket.aggregations().get(GuliEsConstant.AGG_ATTR_NAME).sterms().buckets().array();
                    if (!attrNameBuckets.isEmpty()) {
                        attrInfo.setAttrName(attrNameBuckets.get(0).key().stringValue());
                    }
                    List<String> attrValues = new ArrayList<>();
                    for (var valueBucket : bucket.aggregations().get(GuliEsConstant.AGG_ATTR_VALUE).sterms().buckets().array()) {
                        attrValues.add(valueBucket.key().stringValue());
                    }
                    attrInfo.setAttrValue(attrValues);
                    attrInfos.add(attrInfo);
                }
                esListVo.setAttrs(attrInfos);
            }
        }

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
