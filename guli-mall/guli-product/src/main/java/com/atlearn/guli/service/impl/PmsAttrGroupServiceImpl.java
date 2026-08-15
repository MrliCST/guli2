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
import com.atlearn.guli.domain.bo.PmsAttrAttrgroupRelationBo;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.vo.PmsAttrGroupVo;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.vo.PmsAttrAttrgroupRelationVo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.PmsAttrGroup;
import com.atlearn.guli.domain.PmsAttr;
import com.atlearn.guli.domain.PmsAttrAttrgroupRelation;
import com.atlearn.guli.mapper.PmsAttrGroupMapper;
import com.atlearn.guli.mapper.PmsAttrMapper;
import com.atlearn.guli.mapper.PmsAttrAttrgroupRelationMapper;
import com.atlearn.guli.service.IPmsAttrGroupService;
import com.atlearn.guli.service.IPmsCategoryService;

import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final PmsAttrMapper attrMapper;
    private final PmsAttrAttrgroupRelationMapper relationMapper;
    private final IPmsCategoryService pmsCategoryService;

    // ==================== 属性分组 CRUD ====================

    @Override
    public PmsAttrGroupVo queryById(Long attrGroupId){
        return baseMapper.selectVoById(attrGroupId);
    }

    @Override
    public TableDataInfo<PmsAttrGroupVo> queryPageList(PmsAttrGroupBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = buildQueryWrapper(bo);
        Page<PmsAttrGroupVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<PmsAttrGroupVo> queryList(PmsAttrGroupBo bo) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @SuppressWarnings("null")
    private LambdaQueryWrapper<PmsAttrGroup> buildQueryWrapper(PmsAttrGroupBo bo) {
        LambdaQueryWrapper<PmsAttrGroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAttrGroupId() != null, PmsAttrGroup::getAttrGroupId, bo.getAttrGroupId());  // where attr_group_id = ?
        lqw.eq(bo.getCatelogId() != null, PmsAttrGroup::getCatelogId, bo.getCatelogId());  // where catelog_id = ?
        lqw.like(StringUtils.isNotBlank(bo.getAttrGroupName()), PmsAttrGroup::getAttrGroupName, bo.getAttrGroupName());  // where attr_group_name like ?
        lqw.orderByAsc(PmsAttrGroup::getAttrGroupId);  // order by attr_group_id asc
        return lqw;
    }

    @Override
    public Boolean insertByBo(PmsAttrGroupBo bo) {
        PmsAttrGroup add = MapstructUtils.convert(bo, PmsAttrGroup.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    @Override
    public Boolean updateByBo(PmsAttrGroupBo bo) {
        PmsAttrGroup update = MapstructUtils.convert(bo, PmsAttrGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(PmsAttrGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }

    // ==================== 属性分组-属性值储关联 ====================

    @Override
    @SuppressWarnings("null")
    public List<PmsAttrVo> listAvailableAttrs(Long categoryId) {
        // 查询该分类下所有属性分组已占用的属性id集合
        Set<Long> usedIds = relationMapper.selectUsedAttrIdsByCategoryId(categoryId);
        // 查询该分类下所有属性
        List<PmsAttr> allAttrs = attrMapper.selectList(
            Wrappers.lambdaQuery(PmsAttr.class)
                .eq(PmsAttr::getCatelogId, categoryId)
        );
        // 过滤掉已关联的，转为VO返回
        return allAttrs.stream()
            .filter(a -> !usedIds.contains(a.getAttrId()))
            .map(a -> MapstructUtils.convert(a, PmsAttrVo.class))
            .collect(Collectors.toList());
    }

    @Override
    public TableDataInfo<PmsAttrAttrgroupRelationVo> listRelations(Long attrGroupId, PageQuery pageQuery) {
        Page<PmsAttrAttrgroupRelationVo> page = pageQuery.build();
        List<PmsAttrAttrgroupRelationVo> list = relationMapper.selectVoListByGroupId(page, attrGroupId);
        return TableDataInfo.build(page.setRecords(list));
    }

    @Override
    public Boolean insertRelation(PmsAttrAttrgroupRelationBo bo) {
        PmsAttrAttrgroupRelation add = MapstructUtils.convert(bo, PmsAttrAttrgroupRelation.class);
        return relationMapper.insert(add) > 0;
    }

    @Override
    public Boolean updateRelation(PmsAttrAttrgroupRelationBo bo) {
        // 仅更新排序字段
        PmsAttrAttrgroupRelation update = new PmsAttrAttrgroupRelation();
        update.setId(bo.getId());
        update.setAttrSort(bo.getAttrSort());
        return relationMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteRelationWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return relationMapper.deleteByIds(ids) > 0;
    }

    @Override
    public List<PmsCategoryVo> listTreeCategory() {
        return pmsCategoryService.queryTreeList(new PmsCategoryBo());
    }
}
