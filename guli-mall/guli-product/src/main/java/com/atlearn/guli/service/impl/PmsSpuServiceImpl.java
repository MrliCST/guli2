package com.atlearn.guli.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.domain.bo.PmsCategoryBrandRelationBo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.PmsSpuInfo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.atlearn.guli.mapper.PmsSpuMapper;
import com.atlearn.guli.service.IPmsSpuService;
import com.atlearn.guli.service.IPmsCategoryService;
import com.atlearn.guli.service.IPmsBrandService;

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

    private final PmsSpuMapper baseMapper;
    private final IPmsCategoryService categoryService;
    private final IPmsBrandService brandService;

    @Override
    public Boolean insertByBo(PmsSpuBo bo) {
        PmsSpuInfo add = MapstructUtils.convert(bo, PmsSpuInfo.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    private void validEntityBeforeSave(PmsSpuInfo entity){
        //TODO 做一些数据校验,如唯一约束
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
    public List<PmsAttrGroupWithAttrsVo> listAttrGroupsWithAttrs(Long catelogId) {
        return baseMapper.selectAttrGroupsWithAttrs(catelogId);
    }
}
