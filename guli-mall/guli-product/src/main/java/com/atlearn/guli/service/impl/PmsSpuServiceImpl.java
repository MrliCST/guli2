package com.atlearn.guli.service.impl;

import org.dromara.resource.api.domain.RemoteFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.domain.bo.PmsCategoryBrandRelationBo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.PmsProductAttrValue;
import com.atlearn.guli.domain.PmsSkuImages;
import com.atlearn.guli.domain.PmsSkuInfo;
import com.atlearn.guli.domain.PmsSkuSaleAttrValue;
import com.atlearn.guli.domain.PmsSpuImages;
import com.atlearn.guli.domain.PmsSpuInfo;
import com.atlearn.guli.domain.PmsSpuInfoDesc;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.bo.PmsAttrBo;
import com.atlearn.guli.mapper.PmsSpuInfoMapper;
import com.atlearn.guli.mapper.PmsAttrAttrgroupRelationMapper;
import com.atlearn.guli.mapper.PmsProductAttrValueMapper;
import com.atlearn.guli.mapper.PmsSkuImagesMapper;
import com.atlearn.guli.mapper.PmsSkuInfoMapper;
import com.atlearn.guli.mapper.PmsSkuSaleAttrValueMapper;
import com.atlearn.guli.mapper.PmsSpuImagesMapper;
import com.atlearn.guli.mapper.PmsSpuInfoDescMapper;
import com.atlearn.guli.service.IPmsSpuService;
import com.atlearn.guli.service.IPmsCategoryService;
import com.atlearn.guli.service.IPmsBrandService;
import com.atlearn.guli.service.IPmsAttrService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private final IPmsCategoryService categoryService;
    private final IPmsBrandService brandService;
    private final IPmsAttrService attrService;

    private final PmsSpuInfoMapper baseMapper;
    private final PmsSpuImagesMapper spuImagesMapper;
    private final PmsSpuInfoDescMapper SpuInfoDescMapper;
    private final PmsProductAttrValueMapper productAttrValueMapper;

    private final PmsAttrAttrgroupRelationMapper attrAttrgroupRelationMapper;
    private final PmsSkuInfoMapper skuInfoMapper;
    private final PmsSkuSaleAttrValueMapper skuSaleAttrValueMapper;
    private final PmsSkuImagesMapper skuImagesMapper;

    @Override
    @Transactional
    public Boolean insertByBo(PmsSpuBo bo) {
        PmsSpuBo.SpuInfo spuInfo = bo.getSpu();
        if (spuInfo == null) {
            throw new IllegalArgumentException("spu信息不能为空");
        }
        List<PmsSpuBo.BaseAttr> baseAttrs = spuInfo.getBaseAttrs();
        List<PmsSpuBo.Sku> skus = bo.getSkus();
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
        if(description == null || description.isEmpty()){
            throw new IllegalArgumentException("spu描述不能为空");
        }
        PmsSpuInfoDesc desc = PmsSpuInfoDesc.builder()
                .spuId(spuId)
                .decript(description)
                .build();
        SpuInfoDescMapper.insert(desc);

        // 4. 保存选中的基本属性（前端已携带 attrName，无需查询）
        if (baseAttrs != null && !baseAttrs.isEmpty()) {
            List<PmsProductAttrValue> productAttrValues = new ArrayList<>();
            for (PmsSpuBo.BaseAttr baseAttr : baseAttrs) {
                productAttrValues.add(PmsProductAttrValue.builder()
                        .spuId(spuId)
                        .attrId(baseAttr.getAttrId())
                        .attrName(baseAttr.getAttrName())
                        .attrValue(baseAttr.getAttrValue())
                        .build());
            }
            productAttrValueMapper.insertBatch(productAttrValues);
        }

        // 5. 保存SKU信息 + 销售属性 + SKU图片
        if (skus != null && !skus.isEmpty()) {
            for (PmsSpuBo.Sku sku : skus) {
                // 5a. SKU基本信息
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

                // 5b. SKU销售属性
                List<PmsSpuBo.SaleAttr> skuAttrs = sku.getSkuAttrs();
                if (skuAttrs != null && !skuAttrs.isEmpty()) {
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
                }

                // 5c. SKU图片
                List<String> skuImages = sku.getSkuImages();
                if (skuImages != null && !skuImages.isEmpty()) {
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
                }
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
}
