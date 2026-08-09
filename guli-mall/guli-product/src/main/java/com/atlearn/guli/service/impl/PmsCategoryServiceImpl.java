package com.atlearn.guli.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.atlearn.guli.domain.bo.PmsCategoryBo;
import com.atlearn.guli.domain.vo.PmsCategoryVo;
import com.atlearn.guli.domain.PmsCategory;
import com.atlearn.guli.domain.PmsCategoryBrandRelation;
import com.atlearn.guli.mapper.PmsCategoryMapper;
import com.atlearn.guli.mapper.PmsCategoryBrandRelationMapper;
import com.atlearn.guli.service.IPmsCategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 商品三级分类Service业务层处理
 *
 * @author mayao
 * @date 2026-07-25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsCategoryServiceImpl implements IPmsCategoryService {

    private final PmsCategoryMapper baseMapper;
    private final PmsCategoryBrandRelationMapper cbrMapper;

    /**
     * 查询商品三级分类
     *
     * @param catId 主键
     * @return 商品三级分类
     */
    @Override
    public PmsCategoryVo queryById(Long catId){
        return baseMapper.selectVoById(catId);
    }

    /**
     * 查询符合条件的商品三级分类列表
     * @param bo 查询条件
     * @return 三级分类列表
     */
    @Override
    public List<PmsCategoryVo> queryList(PmsCategoryBo bo) {
        LambdaQueryWrapper<PmsCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询符合条件的商品三级分类树列表
     *
     * @param bo 查询条件
     * @return 商品三级分类树列表
     */
    @Override
    @SuppressWarnings("null")
    public List<PmsCategoryVo> queryTreeList(PmsCategoryBo bo) {
        LambdaQueryWrapper<PmsCategory> lqw = buildQueryWrapper(bo);
        List<PmsCategoryVo> list = baseMapper.selectVoList(lqw);  // 获取P数组

        // return R -> roof+
        // roof -> P      (if P.parentId == 0)
        // P -> field P+  (if Pleft.cid == Pright.parentId)

        // 按 parentCid相同为一组 进行分组，用于构建树形
        // 父节点cid -> 子节点列表
        Map<Long, List<PmsCategoryVo>> parentChildMap = list.stream()
                .collect(Collectors.groupingBy(PmsCategoryVo::getParentCid));
        Comparator<PmsCategoryVo> sortComparator = Comparator.comparing(
                PmsCategoryVo::getSort, Comparator.nullsLast(Long::compareTo));

        parentChildMap.forEach((parentId, children) ->
                children.sort(sortComparator));

        // 为每一个父节点填充已排序的子节点, 获取顶级父节点
        return list.stream()
                .map(vo -> {
                    vo.setChildren(parentChildMap.getOrDefault(vo.getCatId(), List.of()));
                    return vo;
                })
                .filter(vo -> vo.getParentCid() == 0L)
                .sorted(sortComparator)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")  // PmsCategory::getCatId方法由@Data生成，不可能为空，取消警告
    private LambdaQueryWrapper<PmsCategory> buildQueryWrapper(PmsCategoryBo bo) {
        LambdaQueryWrapper<PmsCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCatId() != null, PmsCategory::getCatId, bo.getCatId());  // where catId = ?
        lqw.like(StringUtils.isNotBlank(bo.getName()), PmsCategory::getName, bo.getName());  // where name like ?
        lqw.orderByAsc(PmsCategory::getCatId);  // order by catId asc
        return lqw;
    }

    /**
     * 新增商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(PmsCategoryBo bo) {
        PmsCategory add = MapstructUtils.convert(bo, PmsCategory.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改商品三级分类
     *
     * @param bo 商品三级分类
     * @return 是否修改成功
     */
    @Override
    @Transactional
    @SuppressWarnings("null")
    public Boolean updateByBo(PmsCategoryBo bo) {
        PmsCategory update = MapstructUtils.convert(bo, PmsCategory.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (!flag || bo.getName() == null) {
            return flag;
        }
        // 级联更新中间表的冗余分类名
        cbrMapper.update(null,
            Wrappers.lambdaUpdate(PmsCategoryBrandRelation.class)
                .set(PmsCategoryBrandRelation::getCatelogName, bo.getName())
                .eq(PmsCategoryBrandRelation::getCatelogId, bo.getCatId()));
        return true;
    }

    /**
     * 批量修改商品三级分类 (sort批量更新)
     *
     * @param boList 商品三级分类集合
     * @return 是否修改成功
     */
    @Override
    public Boolean updateBatchByBo(List<PmsCategoryBo> boList) {
        List<PmsCategory> updateList = boList.stream()
            .map(bo -> MapstructUtils.convert(bo, PmsCategory.class))
            .toList();
        updateList.forEach(this::validEntityBeforeSave);
        return baseMapper.updateBatchById(updateList);
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PmsCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除商品三级分类信息
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
