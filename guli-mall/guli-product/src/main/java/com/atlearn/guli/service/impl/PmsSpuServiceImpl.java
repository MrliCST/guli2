package com.atlearn.guli.service.impl;

import org.dromara.resource.api.domain.RemoteFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.domain.bo.PmsCategoryBrandRelationBo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
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
import com.atlearn.guli.mapper.PmsSpuImagesMapper;
import com.atlearn.guli.mapper.PmsSpuInfoDescMapper;
import com.atlearn.guli.service.IPmsSpuService;
import com.atlearn.guli.service.IPmsCategoryService;
import com.atlearn.guli.service.IPmsBrandService;
import com.atlearn.guli.service.IPmsAttrService;

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

    @Override
    @Transactional
    public Boolean insertByBo(PmsSpuBo bo) {
        PmsSpuBo.SpuInfo spuInfo = bo.getSpuInfo();

        // 1. 保存spu信息
        if(spuInfo == null){
            throw new IllegalArgumentException("spu信息不能为空");
        }
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
