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
import com.atlearn.guli.domain.bo.WmsWareSkuBo;
import com.atlearn.guli.domain.vo.WmsWareSkuVo;
import com.atlearn.guli.domain.WmsWareSku;
import com.atlearn.guli.mapper.WmsWareSkuMapper;
import com.atlearn.guli.service.IWmsWareSkuService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 商品库存Service业务层处理
 *
 * @author mayao
 * @date 2026-08-12
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WmsWareSkuServiceImpl implements IWmsWareSkuService {

    private final WmsWareSkuMapper baseMapper;

    /**
     * 查询商品库存
     *
     * @param id 主键
     * @return 商品库存
     */
    @Override
    public WmsWareSkuVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询商品库存列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 商品库存分页列表
     */
    @Override
    public TableDataInfo<WmsWareSkuVo> queryPageList(WmsWareSkuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WmsWareSku> lqw = buildQueryWrapper(bo);
        Page<WmsWareSkuVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的商品库存列表
     *
     * @param bo 查询条件
     * @return 商品库存列表
     */
    @Override
    public List<WmsWareSkuVo> queryList(WmsWareSkuBo bo) {
        LambdaQueryWrapper<WmsWareSku> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WmsWareSku> buildQueryWrapper(WmsWareSkuBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WmsWareSku> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(WmsWareSku::getId);
        lqw.eq(bo.getSkuId() != null, WmsWareSku::getSkuId, bo.getSkuId());
        lqw.eq(bo.getWareId() != null, WmsWareSku::getWareId, bo.getWareId());
        return lqw;
    }

    /**
     * 新增商品库存
     *
     * @param bo 商品库存
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(WmsWareSkuBo bo) {
        WmsWareSku add = MapstructUtils.convert(bo, WmsWareSku.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品库存
     *
     * @param bo 商品库存
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(WmsWareSkuBo bo) {
        WmsWareSku update = MapstructUtils.convert(bo, WmsWareSku.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WmsWareSku entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除商品库存信息
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
