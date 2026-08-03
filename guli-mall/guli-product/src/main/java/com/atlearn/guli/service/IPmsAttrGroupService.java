package com.atlearn.guli.service;

import com.atlearn.guli.domain.PmsAttrGroup;
import com.atlearn.guli.domain.vo.PmsAttrGroupVo;
import com.atlearn.guli.domain.bo.PmsAttrGroupBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 属性分组Service接口
 *
 * @author mayao
 * @date 2026-08-02
 */
public interface IPmsAttrGroupService {

    /**
     * 查询属性分组
     *
     * @param attrGroupId 主键
     * @return 属性分组
     */
    PmsAttrGroupVo queryById(Long attrGroupId);

    /**
     * 分页查询属性分组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 属性分组分页列表
     */
    TableDataInfo<PmsAttrGroupVo> queryPageList(PmsAttrGroupBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的属性分组列表
     *
     * @param bo 查询条件
     * @return 属性分组列表
     */
    List<PmsAttrGroupVo> queryList(PmsAttrGroupBo bo);

    /**
     * 新增属性分组
     *
     * @param bo 属性分组
     * @return 是否新增成功
     */
    Boolean insertByBo(PmsAttrGroupBo bo);

    /**
     * 修改属性分组
     *
     * @param bo 属性分组
     * @return 是否修改成功
     */
    Boolean updateByBo(PmsAttrGroupBo bo);

    /**
     * 校验并批量删除属性分组信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
