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
import com.atlearn.guli.domain.bo.WmsPurchaseBo;
import com.atlearn.guli.domain.vo.WmsPurchaseVo;
import com.atlearn.guli.domain.WmsPurchase;
import com.atlearn.guli.mapper.WmsPurchaseMapper;
import com.atlearn.guli.service.IWmsPurchaseService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 采购信息Service业务层处理
 *
 * @author mayao
 * @date 2026-08-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WmsPurchaseServiceImpl implements IWmsPurchaseService {

    private final WmsPurchaseMapper baseMapper;

    /**
     * 查询采购信息
     *
     * @param id 主键
     * @return 采购信息
     */
    @Override
    public WmsPurchaseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询采购信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购信息分页列表
     */
    @Override
    public TableDataInfo<WmsPurchaseVo> queryPageList(WmsPurchaseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsPurchase> lqw = buildQueryWrapper(bo);
        Page<WmsPurchaseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的采购信息列表
     *
     * @param bo 查询条件
     * @return 采购信息列表
     */
    @Override
    public List<WmsPurchaseVo> queryList(WmsPurchaseBo bo) {
        LambdaQueryWrapper<WmsPurchase> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsPurchase> buildQueryWrapper(WmsPurchaseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsPurchase> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(WmsPurchase::getId);
        lqw.like(StringUtils.isNotBlank(bo.getAssigneeName()), WmsPurchase::getAssigneeName, bo.getAssigneeName());
        return lqw;
    }

    /**
     * 新增采购信息
     *
     * @param bo 采购信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(WmsPurchaseBo bo) {
        WmsPurchase add = MapstructUtils.convert(bo, WmsPurchase.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改采购信息
     *
     * @param bo 采购信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(WmsPurchaseBo bo) {
        WmsPurchase update = MapstructUtils.convert(bo, WmsPurchase.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WmsPurchase entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除采购信息信息
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
