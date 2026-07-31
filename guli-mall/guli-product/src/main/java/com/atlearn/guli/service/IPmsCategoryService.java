package com.atlearn.guli.service;

import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 商品三级分类Service接口
 *
 * @author mayao
 * @date 2026-07-25
 */
public interface IPmsCategoryService {

    /**
     * 查询商品三级分类
     *
     * @param catId 主键
     * @return 商品三级分类
     */
    PmsCategoryVo queryById(Long catId);

    /**
     * 查询符合条件的商品三级分类列表
     * @param bo
     * @return
     */
    List<PmsCategoryVo> queryList(PmsCategoryBo bo);

    /**
     * 查询符合条件的商品三级分类树列表
     *
     * @param bo 查询条件
     * @return 商品三级分类列表
     */
    List<PmsCategoryVo> queryTreeList(PmsCategoryBo bo);

    /**
     * 新增商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否新增成功
     */
    Boolean insertByBo(PmsCategoryBo bo);

    /**
     * 修改商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否修改成功
     */
    Boolean updateByBo(PmsCategoryBo bo);

    /**
     * 批量修改商品三级分类
     *
     * @param boList 商品三级分类集合
     * @return 是否修改成功
     */
    Boolean updateBatchByBo(List<PmsCategoryBo> boList);

    /**
     * 校验并批量删除商品三级分类信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
