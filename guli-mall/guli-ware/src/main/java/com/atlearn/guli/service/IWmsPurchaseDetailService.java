package com.atlearn.guli.service;

import com.atlearn.guli.domain.WmsPurchaseDetail;
import com.atlearn.guli.domain.vo.WmsPurchaseDetailVo;
import com.atlearn.guli.domain.bo.WmsPurchaseDetailBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 采购单详情Service接口
 *
 * @author mayao
 * @date 2026-08-13
 */
public interface IWmsPurchaseDetailService {

    /**
     * 查询采购单详情
     *
     * @param id 主键
     * @return 采购单详情
     */
    WmsPurchaseDetailVo queryById(Long id);

    /**
     * 分页查询采购单详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购单详情分页列表
     */
    TableDataInfo<WmsPurchaseDetailVo> queryPageList(WmsPurchaseDetailBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的采购单详情列表
     *
     * @param bo 查询条件
     * @return 采购单详情列表
     */
    List<WmsPurchaseDetailVo> queryList(WmsPurchaseDetailBo bo);

    /**
     * 新增采购单详情
     *
     * @param bo 采购单详情
     * @return 是否新增成功
     */
    Boolean insertByBo(WmsPurchaseDetailBo bo);

    /**
     * 修改采购单详情
     *
     * @param bo 采购单详情
     * @return 是否修改成功
     */
    Boolean updateByBo(WmsPurchaseDetailBo bo);

    /**
     * 校验并批量删除采购单详情信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
