package com.atlearn.guli.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.atlearn.guli.domain.bo.WmsWareInfoBo;
import com.atlearn.guli.domain.vo.WmsWareInfoVo;
import com.atlearn.guli.domain.WmsWareInfo;
import com.atlearn.guli.mapper.WmsWareInfoMapper;
import com.atlearn.guli.service.IWmsWareInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 仓库信息Service业务层处理
 *
 * @author mayao
 * @date 2026-08-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WmsWareInfoServiceImpl implements IWmsWareInfoService {

    private final WmsWareInfoMapper baseMapper;

    /**
     * 查询仓库信息
     *
     * @param id 主键
     * @return 仓库信息
     */
    @Override
    public WmsWareInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询仓库信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 仓库信息分页列表
     */
    @Override
    public TableDataInfo<WmsWareInfoVo> queryPageList(WmsWareInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsWareInfo> lqw = buildQueryWrapper(bo);
        Page<WmsWareInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的仓库信息列表
     *
     * @param bo 查询条件
     * @return 仓库信息列表
     */
    @Override
    public List<WmsWareInfoVo> queryList(WmsWareInfoBo bo) {
        LambdaQueryWrapper<WmsWareInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsWareInfo> buildQueryWrapper(WmsWareInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsWareInfo> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(WmsWareInfo::getId);
        lqw.like(StringUtils.isNotBlank(bo.getName()), WmsWareInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), WmsWareInfo::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getAreacode()), WmsWareInfo::getAreacode, bo.getAreacode());
        return lqw;
    }

    /**
     * 新增仓库信息
     *
     * @param bo 仓库信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(WmsWareInfoBo bo) {
        WmsWareInfo add = MapstructUtils.convert(bo, WmsWareInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改仓库信息
     *
     * @param bo 仓库信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(WmsWareInfoBo bo) {
        WmsWareInfo update = MapstructUtils.convert(bo, WmsWareInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WmsWareInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除仓库信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
