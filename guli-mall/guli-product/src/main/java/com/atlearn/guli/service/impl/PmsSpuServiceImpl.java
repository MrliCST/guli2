package com.atlearn.guli.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.dromara.resource.api.domain.RemoteFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atlearn.guli.RemoteCouponService;
import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.domain.PmsProductAttrValue;
import com.atlearn.guli.domain.PmsSkuImages;
import com.atlearn.guli.domain.PmsSkuInfo;
import com.atlearn.guli.domain.PmsSkuSaleAttrValue;
import com.atlearn.guli.domain.PmsSpuImages;
import com.atlearn.guli.domain.PmsSpuInfo;
import com.atlearn.guli.domain.PmsSpuInfoDesc;
import com.atlearn.guli.domain.bo.PmsAttrBo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.bo.PmsCategoryBrandRelationBo;
import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.domain.bo.RmeSkuFullReductionBo;
import com.atlearn.guli.domain.bo.RmeSkuLadderBo;
import com.atlearn.guli.domain.bo.RmeSpuBoundsBo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.vo.PmsBrandVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.SkuEsModel;
import org.dromara.easyes.core.kernel.BaseEsMapper;
import com.atlearn.guli.mapper.PmsAttrAttrgroupRelationMapper;
import com.atlearn.guli.mapper.PmsProductAttrValueMapper;
import com.atlearn.guli.mapper.PmsSkuImagesMapper;
import com.atlearn.guli.mapper.PmsSkuInfoMapper;
import com.atlearn.guli.mapper.PmsSkuSaleAttrValueMapper;
import com.atlearn.guli.mapper.PmsSpuImagesMapper;
import com.atlearn.guli.mapper.PmsSpuInfoDescMapper;
import com.atlearn.guli.mapper.PmsSpuInfoMapper;
import com.atlearn.guli.service.IPmsAttrService;
import com.atlearn.guli.service.IPmsBrandService;
import com.atlearn.guli.service.IPmsCategoryService;
import com.atlearn.guli.service.IPmsSpuService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SPU信息Service业务层处理
 *
 * @author mayao
 * @date 2026-08-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsSpuServiceImpl implements IPmsSpuService {

    // 提供查询数据服务
    private final IPmsCategoryService categoryService;
    private final IPmsBrandService brandService;
    private final IPmsAttrService attrService;

    // coupon模块远程服务
    @DubboReference
    private final RemoteCouponService remoteCouponService;
    
    // ware模块远程服务
    @DubboReference
    private final RemoteWareService remoteWareService;

    // 获取 属性组内嵌属性值储 的数据
    private final PmsAttrAttrgroupRelationMapper attrAttrgroupRelationMapper;

    // 插入 spu 相关的信息
    private final PmsSpuInfoMapper baseMapper;
    private final PmsSpuImagesMapper spuImagesMapper;
    private final PmsSpuInfoDescMapper SpuInfoDescMapper;
    private final PmsProductAttrValueMapper productAttrValueMapper;
    private final PmsSkuInfoMapper skuInfoMapper;
    private final PmsSkuSaleAttrValueMapper skuSaleAttrValueMapper;
    private final PmsSkuImagesMapper skuImagesMapper;
    private final BaseEsMapper<SkuEsModel> skuEsMapper;

    @Override
    @Transactional
    @GlobalTransactional
    public Boolean insertByBo(PmsSpuBo bo) {
        // 提取bo中的内容
        PmsSpuBo.SpuInfo spuInfo = bo.getSpu();
        List<PmsSpuBo.Sku> skus = bo.getSkus();

        // 1. 保存spu信息
        PmsSpuInfo baseSpuInfo = PmsSpuInfo.builder()  
                .spuName(spuInfo.getSpuName())
                .catalogId(spuInfo.getCatalogId())
                .brandId(spuInfo.getBrandId())
                .weight(spuInfo.getWeight())
                .publishStatus(spuInfo.getPublishStatus())
                .build();
        baseMapper.insert(baseSpuInfo);
        Long spuId = baseSpuInfo.getId();

        // 2. 保存spu图片
        List<RemoteFile> imgAlbum = spuInfo.getImgAlbum();
        if (imgAlbum == null || imgAlbum.isEmpty()) {
            throw new IllegalArgumentException("商品图集不能为空");
        }
        List<PmsSpuImages> images = new ArrayList<>();
        for (int i = 0; i < imgAlbum.size(); i++) {
            RemoteFile img = imgAlbum.get(i);
            images.add(PmsSpuImages.builder()
                .spuId(spuId)
                .imgUrl(img.getUrl())
                .imgName(img.getName())
                .imgSort((long) i)
                .defaultImg(i == 0 ? 1L : 0L)
                .build());
        }
        spuImagesMapper.insertBatch(images);

        // 3. 保存spu描述
        String description = spuInfo.getSpuDescription();
        PmsSpuInfoDesc desc = PmsSpuInfoDesc.builder()
                .spuId(spuId)
                .decript(description)
                .build();
        SpuInfoDescMapper.insert(desc);

        // 4. 保存购物积分和成长值
        RmeSpuBoundsBo bound = RmeSpuBoundsBo.builder()
                .spuId(spuId)
                .growBounds(spuInfo.getGrowBounds())    
                .buyBounds(spuInfo.getBuyBounds())
                .build();
        remoteCouponService.insertSpuBoundsByBo(bound);

        // 5. 保存选中的基本属性
        List<PmsSpuBo.BaseAttr> baseAttrs = spuInfo.getBaseAttrs();
        List<PmsProductAttrValue> productAttrValues = baseAttrs.stream()
            .map(attr -> PmsProductAttrValue.builder()
                .spuId(spuId)
                .attrId(attr.getAttrId())
                .attrName(attr.getAttrName())
                .attrValue(attr.getAttrValue())
                .build())
            .collect(Collectors.toList());
        productAttrValueMapper.insertBatch(productAttrValues);

        // 6. 保存多种SKU信息
        for (PmsSpuBo.Sku sku : skus) {
            // 6a. SKU基本信息
            PmsSkuInfo skuInfo = PmsSkuInfo.builder()
                    .spuId(spuId)
                    .catalogId(spuInfo.getCatalogId())
                    .brandId(spuInfo.getBrandId())
                    .skuName(sku.getSkuName())
                    .skuDesc(sku.getSkuDesc())
                    .skuDefaultImg(sku.getSkuDefaultImg())
                    .skuTitle(sku.getSkuTitle())
                    .skuSubtitle(sku.getSkuSubtitle())
                    .price(new BigDecimal(sku.getPrice()))
                    .build();
            skuInfoMapper.insert(skuInfo);
            Long skuId = skuInfo.getSkuId();

            // 6b. SKU销售属性
            List<PmsSpuBo.SaleAttr> skuAttrs = sku.getSkuAttrs();
            List<PmsSkuSaleAttrValue> saleAttrValues = new ArrayList<>();
            for (int i = 0; i < skuAttrs.size(); i++) {
                PmsSpuBo.SaleAttr attr = skuAttrs.get(i);
                saleAttrValues.add(PmsSkuSaleAttrValue.builder()
                        .skuId(skuId)
                        .attrId(attr.getAttrId())
                        .attrName(attr.getAttrName())
                        .attrValue(attr.getAttrValue())
                        .attrSort((long) i)
                        .build());
            }
            skuSaleAttrValueMapper.insertBatch(saleAttrValues);

            // 6c. SKU图片
            List<String> skuImages = sku.getSkuImages();
            List<PmsSkuImages> skuImageList = new ArrayList<>();
            for (int i = 0; i < skuImages.size(); i++) {
                skuImageList.add(PmsSkuImages.builder()
                        .skuId(skuId)
                        .imgUrl(skuImages.get(i))
                        .imgSort((long) i)
                        .defaultImg(i == 0 ? 1L : 0L)
                        .build());
            }
            skuImagesMapper.insertBatch(skuImageList);

            // 6d. 保存 "满几件打几折" 的优惠策略(未填写则跳过)
            if (sku.getFullCount() != null && sku.getDiscount() != null) {
                RmeSkuLadderBo ladder = RmeSkuLadderBo.builder()
                        .skuId(skuId)
                        .fullCount(sku.getFullCount())
                        .discount(sku.getDiscount())
                        .build();
                remoteCouponService.insertSkuLadderByBo(ladder);
            }

            // 6e. 保存 "满几件减几元" 的优惠策略(未填写则跳过)
            if (sku.getFullPrice() != null && sku.getReducePrice() != null) {
                RmeSkuFullReductionBo reduction = RmeSkuFullReductionBo.builder()
                        .skuId(skuId)
                        .fullPrice(new BigDecimal(sku.getFullPrice()))
                        .reducePrice(new BigDecimal(sku.getReducePrice()))
                        .build();
                remoteCouponService.insertSkuFullReductionByBo(reduction);
            }
                    
        }

        return true;
    }

    @Override
    public List<PmsCategoryVo> listTreeCategory() {
        return categoryService.queryTreeList(new PmsCategoryBo());
    }

    @Override
    public List<PmsCategoryBrandRelationVo> listBrandsByCategoryId(Long catelogId) {
        PmsCategoryBrandRelationBo bo = new PmsCategoryBrandRelationBo();
        bo.setCatelogId(catelogId);
        return brandService.queryCbrList(bo);
    }

    @Override
    public List<PmsAttrGroupWithAttrsVo> listBaseAttrs(Long catelogId) {
        return attrAttrgroupRelationMapper.selectAttrGroupsWithBaseAttrs(catelogId);
    }

    @Override
    public List<PmsAttrVo> listSaleAttrs(Long catelogId) {
        PmsAttrBo bo = new PmsAttrBo();
        bo.setCatelogId(catelogId);
        bo.setAttrType(0L); // 0=销售属性
        return attrService.queryList(bo);
    }

    @Override
    public Boolean upToEsearch(Long spuId) {
        // 查询当前spuId对应的所有sku信息
        List<PmsSkuInfo> skuInfos = skuInfoMapper.selectList(
            Wrappers.<PmsSkuInfo>lambdaQuery().eq(x -> x.getSpuId(), spuId)
        );
        if (skuInfos.isEmpty()) {
            return false;
        }

        // 获取远程 map[skuId] => skuAvailableStock 的数据
        List<Long> skuIds = skuInfos.stream().map(skuInfo -> {
            return skuInfo.getSkuId();
        }).collect(Collectors.toList());
        Map<Long,Long> skuAvailableStock = remoteWareService.getSkuAvailableStock(skuIds);

        // 查spu信息，取品牌和分类
        PmsSpuInfo spuInfo = baseMapper.selectById(spuId);
        PmsBrandVo brand = brandService.queryById(spuInfo.getBrandId());
        PmsCategoryVo category = categoryService.queryById(spuInfo.getCatalogId());

        // 联表查询该spu所属分类下、可检索(search_type=1)的属性值，用于搜索/筛选
        List<PmsProductAttrValue> searchAttrs = productAttrValueMapper.selectSearchAttrsBySpuId(spuId, spuInfo.getCatalogId());
        List<SkuEsModel.Attrs> attrs = searchAttrs.stream()
            .map(v -> {
                SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
                attr.setAttrId(v.getAttrId());
                attr.setAttrName(v.getAttrName());
                attr.setAttrValue(v.getAttrValue());
                return attr;
            })
            .collect(Collectors.toList());

        // 构造ES索引数据的 DTO
        List<SkuEsModel> skuEsModels = skuInfos.stream().map(skuInfo -> {
            Long skuId = skuInfo.getSkuId();

            return SkuEsModel.builder()
                .skuId(skuId)
                .spuId(skuInfo.getSpuId())
                .skuTitle(skuInfo.getSkuTitle())
                .skuPrice(skuInfo.getPrice())
                .skuImg(skuInfo.getSkuDefaultImg())
                .saleCount(skuInfo.getSaleCount())
                .brandId(skuInfo.getBrandId())
                .catalogId(skuInfo.getCatalogId())
                .hasStock(skuAvailableStock.getOrDefault(skuId, 0L) > 0)  // 是否有库存
                .hotScore(0L)          // 热度评分(暂无独立热度数据,写死为0)
                .brandName(brand.getName())      // 品牌名称
                .brandImg(brand.getLogo())       // 品牌图片
                .catalogName(category.getName()) // 分类名称
                .attrs(attrs)                    // 商品规格属性
                .build();
        }).collect(Collectors.toList());

        // 上架到ES索引库
        skuEsMapper.insertBatch(skuEsModels);

        return true;
    }
}
