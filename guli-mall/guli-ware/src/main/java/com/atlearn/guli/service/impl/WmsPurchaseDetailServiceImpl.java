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
import com.atlearn.guli.domain.bo.WmsPurchaseDetailBo;
import com.atlearn.guli.domain.vo.WmsPurchaseDetailVo;
import com.atlearn.guli.domain.WmsPurchaseDetail;
import com.atlearn.guli.mapper.WmsPurchaseDetailMapper;
import com.atlearn.guli.service.IWmsPurchaseDetailService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 采购单详情Service业务层处理
 *
 * @author mayao
 * @date 2026-08-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WmsPurchaseDetailServiceImpl implements IWmsPurchaseDetailService {

    private final WmsPurchaseDetailMapper baseMapper;

    /**
     * 查询采购单详情
     *
     * @param id 主键
     * @return 采购单详情
     */
    @Override
    public WmsPurchaseDetailVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询采购单详情列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 采购单详情分页列表
     */
    @Override
    public TableDataInfo<WmsPurchaseDetailVo> queryPageList(WmsPurchaseDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsPurchaseDetail> lqw = buildQueryWrapper(bo);
        Page<WmsPurchaseDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的采购单详情列表
     *
     * @param bo 查询条件
     * @return 采购单详情列表
     */
    @Override
    public List<WmsPurchaseDetailVo> queryList(WmsPurchaseDetailBo bo) {
        LambdaQueryWrapper<WmsPurchaseDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsPurchaseDetail> buildQueryWrapper(WmsPurchaseDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsPurchaseDetail> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(WmsPurchaseDetail::getId);
        lqw.eq(bo.getPurchaseId() != null, WmsPurchaseDetail::getPurchaseId, bo.getPurchaseId());
        lqw.eq(bo.getSkuId() != null, WmsPurchaseDetail::getSkuId, bo.getSkuId());
        return lqw;
    }

    /**
     * 新增采购单详情
     *
     * @param bo 采购单详情
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(WmsPurchaseDetailBo bo) {
        WmsPurchaseDetail add = MapstructUtils.convert(bo, WmsPurchaseDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改采购单详情
     *
     * @param bo 采购单详情
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(WmsPurchaseDetailBo bo) {
        WmsPurchaseDetail update = MapstructUtils.convert(bo, WmsPurchaseDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WmsPurchaseDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除采购单详情信息
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
