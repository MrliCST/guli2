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
import com.atlearn.guli.domain.bo.PmsBrandBo;
import com.atlearn.guli.domain.vo.PmsBrandVo;
import com.atlearn.guli.domain.PmsBrand;
import com.atlearn.guli.mapper.PmsBrandMapper;
import com.atlearn.guli.service.IPmsBrandService;

import java.util.List;
import java.util.Collection;

/**
 * 品牌Service业务层处理
 *
 * @author mayao
 * @date 2026-07-30
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PmsBrandServiceImpl implements IPmsBrandService {

    private final PmsBrandMapper baseMapper;

    /**
     * 查询品牌
     *
     * @param brandId 主键
     * @return 品牌
     */
    @Override
    public PmsBrandVo queryById(Long brandId){
        return baseMapper.selectVoById(brandId);
    }

    /**
     * 分页查询品牌列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 品牌分页列表
     */
    @Override
    public TableDataInfo<PmsBrandVo> queryPageList(PmsBrandBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PmsBrand> lqw = buildQueryWrapper(bo);
        Page<PmsBrandVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的品牌列表
     *
     * @param bo 查询条件
     * @return 品牌列表
     */
    @Override
    public List<PmsBrandVo> queryList(PmsBrandBo bo) {
        LambdaQueryWrapper<PmsBrand> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @SuppressWarnings("null")
    private LambdaQueryWrapper<PmsBrand> buildQueryWrapper(PmsBrandBo bo) {
        LambdaQueryWrapper<PmsBrand> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(PmsBrand::getBrandId);
        lqw.like(StringUtils.isNotBlank(bo.getName()), PmsBrand::getName, bo.getName());
        return lqw;
    }

    /**
     * 新增品牌
     *
     * @param bo 品牌
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(PmsBrandBo bo) {
        PmsBrand add = MapstructUtils.convert(bo, PmsBrand.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBrandId(add.getBrandId());
        }
        return flag;
    }

    /**
     * 修改品牌
     *
     * @param bo 品牌
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(PmsBrandBo bo) {
        PmsBrand update = MapstructUtils.convert(bo, PmsBrand.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PmsBrand entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除品牌信息
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
