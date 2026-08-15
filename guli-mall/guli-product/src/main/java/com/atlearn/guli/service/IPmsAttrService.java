package com.atlearn.guli.service;

import com.atlearn.guli.domain.PmsAttr;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.bo.PmsAttrBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性Service接口
 *
 * @author mayao
 * @date 2026-08-03
 */
public interface IPmsAttrService {

    /**
     * 查询商品属性
     *
     * @param attrId 主键
     * @return 商品属性
     */
    PmsAttrVo queryById(Long attrId);

    /**
     * 分页查询商品属性列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品属性分页列表
     */
    TableDataInfo<PmsAttrVo> queryPageList(PmsAttrBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品属性列表
     *
     * @param bo 查询条件
     * @return 商品属性列表
     */
    List<PmsAttrVo> queryList(PmsAttrBo bo);

    /**
     * 新增商品属性
     *
     * @param bo 商品属性
     * @return 是否新增成功
     */
    Boolean insertByBo(PmsAttrBo bo);

    /**
     * 修改商品属性
     *
     * @param bo 商品属性
     * @return 是否修改成功
     */
    Boolean updateByBo(PmsAttrBo bo);

    /**
     * 校验并批量删除商品属性信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取分类树
     */
    List<PmsCategoryVo> listTreeCategory();
}
