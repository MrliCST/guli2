package com.atlearn.guli.service;

import com.atlearn.guli.domain.bo.PmsSpuBo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.vo.PmsCategoryBrandRelationVo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.atlearn.guli.domain.vo.PmsAttrVo;

import java.util.List;

/**
 * SPU信息Service接口
 *
 * @author mayao
 * @date 2026-08-08
 */
public interface IPmsSpuService {

    /**
     * 新增spu信息
     */
    Boolean insertByBo(PmsSpuBo bo);

    /**
     * 获取分类树
     */
    List<PmsCategoryVo> listTreeCategory();

    /**
     * 根据分类id查询关联的品牌列表
     */
    List<PmsCategoryBrandRelationVo> listBrandsByCategoryId(Long catelogId);

    /**
     * 根据分类id查询属性分组及其属性列表
     */
    List<PmsAttrGroupWithAttrsVo> listBaseAttrs(Long catelogId);

    /**
     * 根据分类id查询销售属性列表（不需要分组）
     */
    List<PmsAttrVo> listSaleAttrs(Long catelogId);

    /**
     * 将spu信息上架到es中
     * @param spuId
     * @return
     */
    Boolean upToEsearch(Long spuId);
}
