package com.atlearn.guli.service;

import com.atlearn.guli.domain.WmsPurchase;
import com.atlearn.guli.domain.vo.WmsPurchaseVo;
import com.atlearn.guli.domain.bo.WmsPurchaseBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 采购信息Service接口
 *
 * @author mayao
 * @date 2026-08-13
 */
public interface IWmsPurchaseService {

    /**
     * 查询采购信息
     *
     * @param id 主键
     * @return 采购信息
     */
    WmsPurchaseVo queryById(Long id);

    /**
     * 分页查询采购信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购信息分页列表
     */
    TableDataInfo<WmsPurchaseVo> queryPageList(WmsPurchaseBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的采购信息列表
     *
     * @param bo 查询条件
     * @return 采购信息列表
     */
    List<WmsPurchaseVo> queryList(WmsPurchaseBo bo);

    /**
     * 新增采购信息
     *
     * @param bo 采购信息
     * @return 是否新增成功
     */
    Boolean insertByBo(WmsPurchaseBo bo);

    /**
     * 修改采购信息
     *
     * @param bo 采购信息
     * @return 是否修改成功
     */
    Boolean updateByBo(WmsPurchaseBo bo);

    /**
     * 校验并批量删除采购信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
