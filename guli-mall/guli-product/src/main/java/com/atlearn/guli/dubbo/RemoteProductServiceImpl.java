package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.domain.PmsSkuInfo;
import com.atlearn.guli.domain.PmsSkuSaleAttrValue;
import com.atlearn.guli.domain.vo.RmeSkuInfoVo;
import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVo;
import com.atlearn.guli.mapper.PmsSkuInfoMapper;
import com.atlearn.guli.mapper.PmsSkuSaleAttrValueMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 远程商品服务实现
 *
 * @author guli
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteProductServiceImpl implements RemoteProductService {

    private final PmsSkuSaleAttrValueMapper skuSaleAttrValueMapper;
    private final PmsSkuInfoMapper skuInfoMapper;

    // 获取 saleAtrr 信息
    @Override
    public List<RmeSkuSaleAttrValueVo> getSkuSaleAttrValueListBySkuId(Long skuId) {
        List<PmsSkuSaleAttrValue> list = skuSaleAttrValueMapper.selectList(
            Wrappers.<PmsSkuSaleAttrValue>lambdaQuery().eq(x -> x.getSkuId(), skuId)
        );
        return list.stream()
            .map(item -> BeanUtil.copyProperties(item, RmeSkuSaleAttrValueVo.class))
            .collect(Collectors.toList());
    }

    // 获取 SKU 信息
    @Override
    public RmeSkuInfoVo getSkuInfoBySkuId(Long skuId) {
        PmsSkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        if (skuInfo == null) {
            return null;
        }
        return BeanUtil.copyProperties(skuInfo, RmeSkuInfoVo.class); // 源 -> 目标 的拷贝
    }

}
