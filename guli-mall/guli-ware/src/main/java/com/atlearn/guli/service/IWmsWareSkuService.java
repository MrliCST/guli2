package com.atlearn.guli.service;

import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.domain.vo.WmsWareSkuVo;
import com.atlearn.guli.domain.bo.WmsWareSkuBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 商品库存Service接口
 *
 * @author mayao
 * @date 2026-08-12
 */
public interface IWmsWareSkuService {

    /**
     * 查询商品库存
     *
     * @param id 主键
     * @return 商品库存
     */
    WmsWareSkuVo queryById(Long id);

    /**
     * 分页查询商品库存列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品库存分页列表
     */
    TableDataInfo<WmsWareSkuVo> queryPageList(WmsWareSkuBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的商品库存列表
     *
     * @param bo 查询条件
     * @return 商品库存列表
     */
    List<WmsWareSkuVo> queryList(WmsWareSkuBo bo);

    /**
     * 新增商品库存
     *
     * @param bo 商品库存
     * @return 是否新增成功
     */
    Boolean insertByBo(WmsWareSkuBo bo);

    /**
     * 修改商品库存
     *
     * @param bo 商品库存
     * @return 是否修改成功
     */
    Boolean updateByBo(WmsWareSkuBo bo);

    /**
     * 校验并批量删除商品库存信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
