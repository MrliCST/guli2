package com.atlearn.guli.dubbo;

import cn.hutool.core.bean.BeanUtil;
import com.atlearn.guli.RemoteProductService;
import com.atlearn.guli.domain.PmsSkuSaleAttrValue;
import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVO;
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

    @Override
    public List<RmeSkuSaleAttrValueVO> getSkuSaleAttrValueListBySkuId(Long skuId) {
        List<PmsSkuSaleAttrValue> list = skuSaleAttrValueMapper.selectList(
            Wrappers.<PmsSkuSaleAttrValue>lambdaQuery().eq(x -> x.getSkuId(), skuId)
        );
        return list.stream()
            .map(item -> BeanUtil.copyProperties(item, RmeSkuSaleAttrValueVO.class))
            .collect(Collectors.toList());
    }

}
