package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import com.atlearn.guli.RemoteCouponService;
import com.atlearn.guli.domain.RemoteSkuFullReductionBo;
import com.atlearn.guli.domain.RemoteSkuLadderBo;
import com.atlearn.guli.domain.RemoteSpuBoundsBo;
import com.atlearn.guli.domain.SmsSkuFullReduction;
import com.atlearn.guli.domain.SmsSkuLadder;
import com.atlearn.guli.domain.SmsSpuBounds;
import com.atlearn.guli.mapper.SmsSkuFullReductionMapper;
import com.atlearn.guli.mapper.SmsSkuLadderMapper;
import com.atlearn.guli.mapper.SmsSpuBoundsMapper;

/**
 * 优惠券远程调用服务实现
 *
 * @author mayao
 * @date 2026-08-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class RemoteCouponServiceImpl implements RemoteCouponService {

    // 满几减免 mapper
    private final SmsSkuFullReductionMapper skuFullReductionMapper;

    // 满几打折
    private final SmsSkuLadderMapper skuLadderMapper;

    // spu积分与成长信息
    private final SmsSpuBoundsMapper spuBoundsMapper;

    /**
     * 新增sku满几减免信息
     */
    @Override
    public Boolean insertSkuFullReductionByBo(RemoteSkuFullReductionBo bo) {
        SmsSkuFullReduction entity = BeanUtil.copyProperties(bo, SmsSkuFullReduction.class);
        return skuFullReductionMapper.insert(entity) > 0;
    }

    /**
     * 新增sku满几打折信息
     */
    @Override
    public Boolean insertSkuLadderByBo(RemoteSkuLadderBo bo) {
        SmsSkuLadder entity = BeanUtil.copyProperties(bo, SmsSkuLadder.class);
        return skuLadderMapper.insert(entity) > 0;
    }

    /**
     * 新增spu积分与成长信息
     */
    @Override
    public Boolean insertSpuBoundsByBo(RemoteSpuBoundsBo bo) {
        SmsSpuBounds entity = BeanUtil.copyProperties(bo, SmsSpuBounds.class);
        return spuBoundsMapper.insert(entity) > 0;
    }

}
