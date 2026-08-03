package com.atlearn.guli.service;

import com.atlearn.guli.domain.vo.PmsBrandVo;
import com.atlearn.guli.domain.bo.PmsBrandBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 品牌Service接口
 *
 * @author mayao
 * @date 2026-07-30
 */
public interface IPmsBrandService {

    /**
     * 查询品牌
     *
     * @param brandId 主键
     * @return 品牌
     */
    PmsBrandVo queryById(Long brandId);

    /**
     * 分页查询品牌列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 品牌分页列表
     */
    TableDataInfo<PmsBrandVo> queryPageList(PmsBrandBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的品牌列表
     *
     * @param bo 查询条件
     * @return 品牌列表
     */
    List<PmsBrandVo> queryList(PmsBrandBo bo);

    /**
     * 新增品牌
     *
     * @param bo 品牌
     * @return 是否新增成功
     */
    Boolean insertByBo(PmsBrandBo bo);

    /**
     * 修改品牌
     *
     * @param bo 品牌
     * @return 是否修改成功
     */
    Boolean updateByBo(PmsBrandBo bo);

    /**
     * 校验并批量删除品牌信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
