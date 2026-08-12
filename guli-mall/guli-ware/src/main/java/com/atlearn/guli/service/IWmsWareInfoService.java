package com.atlearn.guli.service;

import com.atlearn.guli.domain.WmsWareInfo;
import com.atlearn.guli.domain.vo.WmsWareInfoVo;
import com.atlearn.guli.domain.bo.WmsWareInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 仓库信息Service接口
 *
 * @author mayao
 * @date 2026-08-12
 */
public interface IWmsWareInfoService {

    /**
     * 查询仓库信息
     *
     * @param id 主键
     * @return 仓库信息
     */
    WmsWareInfoVo queryById(Long id);

    /**
     * 分页查询仓库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 仓库信息分页列表
     */
    TableDataInfo<WmsWareInfoVo> queryPageList(WmsWareInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的仓库信息列表
     *
     * @param bo 查询条件
     * @return 仓库信息列表
     */
    List<WmsWareInfoVo> queryList(WmsWareInfoBo bo);

    /**
     * 新增仓库信息
     *
     * @param bo 仓库信息
     * @return 是否新增成功
     */
    Boolean insertByBo(WmsWareInfoBo bo);

    /**
     * 修改仓库信息
     *
     * @param bo 仓库信息
     * @return 是否修改成功
     */
    Boolean updateByBo(WmsWareInfoBo bo);

    /**
     * 校验并批量删除仓库信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
