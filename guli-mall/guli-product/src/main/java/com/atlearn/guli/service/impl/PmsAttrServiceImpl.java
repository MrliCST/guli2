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
import com.atlearn.guli.domain.bo.PmsAttrBo;
import com.atlearn.guli.domain.vo.PmsAttrVo;
import com.atlearn.guli.domain.PmsAttr;
import com.atlearn.guli.mapper.PmsAttrMapper;
import com.atlearn.guli.service.IPmsAttrService;

import java.util.List;
import java.util.Collection;

/**
 * 商品属性Service业务层处理
 *
 * @author mayao
 * @date 2026-08-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsAttrServiceImpl implements IPmsAttrService {

    private final PmsAttrMapper baseMapper;

    /**
     * 查询商品属性
     *
     * @param attrId 主键
     * @return 商品属性
     */
    @Override
    public PmsAttrVo queryById(Long attrId){
        return baseMapper.selectVoById(attrId);
    }

    /**
     * 分页查询商品属性列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品属性分页列表
     */
    @Override
    public TableDataInfo<PmsAttrVo> queryPageList(PmsAttrBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PmsAttr> lqw = buildQueryWrapper(bo);
        Page<PmsAttrVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品属性列表
     *
     * @param bo 查询条件
     * @return 商品属性列表
     */
    @Override
    public List<PmsAttrVo> queryList(PmsAttrBo bo) {
        LambdaQueryWrapper<PmsAttr> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @SuppressWarnings("null")
    private LambdaQueryWrapper<PmsAttr> buildQueryWrapper(PmsAttrBo bo) {
        LambdaQueryWrapper<PmsAttr> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(PmsAttr::getAttrId);
        lqw.like(StringUtils.isNotBlank(bo.getAttrName()), PmsAttr::getAttrName, bo.getAttrName());
        lqw.eq(bo.getValueType() != null, PmsAttr::getValueType, bo.getValueType());
        lqw.eq(bo.getShowDesc() != null, PmsAttr::getShowDesc, bo.getShowDesc());
        return lqw;
    }

    /**
     * 新增商品属性
     *
     * @param bo 商品属性
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(PmsAttrBo bo) {
        PmsAttr add = MapstructUtils.convert(bo, PmsAttr.class);
        validEntityBeforeSave(add);
        return baseMapper.insert(add) > 0;
    }

    /**
     * 修改商品属性
     *
     * @param bo 商品属性
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(PmsAttrBo bo) {
        PmsAttr update = MapstructUtils.convert(bo, PmsAttr.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PmsAttr entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除商品属性信息
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
