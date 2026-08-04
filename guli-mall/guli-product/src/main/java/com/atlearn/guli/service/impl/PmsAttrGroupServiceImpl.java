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
import com.atlearn.guli.domain.bo.PmsAttrGroupBo;
import com.atlearn.guli.domain.vo.PmsAttrGroupVo;
import com.atlearn.guli.domain.PmsAttrGroup;
import com.atlearn.guli.mapper.PmsAttrGroupMapper;
import com.atlearn.guli.service.IPmsAttrGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 属性分组Service业务层处理
 *
 * @author mayao
 * @date 2026-08-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsAttrGroupServiceImpl implements IPmsAttrGroupService {

    private final PmsAttrGroupMapper baseMapper;

    /**
     * 查询属性分组
     *
     * @param attrGroupId 主键
     * @return 属性分组
     */
    @Override
    public PmsAttrGroupVo queryById(Long attrGroupId){
        return baseMapper.selectVoById(attrGroupId);
    }

    /**
     * 分页查询属性分组列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 属性分组分页列表
     */
    @Override
    public TableDataInfo<PmsAttrGroupVo> queryPageList(PmsAttrGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = buildQueryWrapper(bo);
        Page<PmsAttrGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的属性分组列表
     *
     * @param bo 查询条件
     * @return 属性分组列表
     */
    @Override
    public List<PmsAttrGroupVo> queryList(PmsAttrGroupBo bo) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @SuppressWarnings("null")
    private LambdaQueryWrapper<PmsAttrGroup> buildQueryWrapper(PmsAttrGroupBo bo) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAttrGroupId() != null, PmsAttrGroup::getAttrGroupId, bo.getAttrGroupId());
        lqw.eq(bo.getCatelogId() != null, PmsAttrGroup::getCatelogId, bo.getCatelogId());
        lqw.like(StringUtils.isNotBlank(bo.getAttrGroupName()), PmsAttrGroup::getAttrGroupName, bo.getAttrGroupName());
        lqw.orderByAsc(PmsAttrGroup::getAttrGroupId);
        return lqw;
    }

    /**
     * 新增属性分组
     *
     * @param bo 属性分组
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(PmsAttrGroupBo bo) {
        PmsAttrGroup add = MapstructUtils.convert(bo, PmsAttrGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAttrGroupId(add.getAttrGroupId());
        }
        return flag;
    }

    /**
     * 修改属性分组
     *
     * @param bo 属性分组
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(PmsAttrGroupBo bo) {
        PmsAttrGroup update = MapstructUtils.convert(bo, PmsAttrGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PmsAttrGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除属性分组信息
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
