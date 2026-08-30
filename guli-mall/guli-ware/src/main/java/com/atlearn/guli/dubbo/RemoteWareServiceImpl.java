package com.atlearn.guli.dubbo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.atlearn.guli.RemoteWareService;
import com.atlearn.guli.mapper.WmsWareSkuMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoteWareServiceImpl implements RemoteWareService{

    // 分元转换 | 1元 = 100分
    private static final Long CENTS_PER_YUAN = 100L;

    private final WmsWareSkuMapper wareSkuMapper;

    @Override
    public Map<Long, Long> getSkuAvailableStock(List<Long> skuIds) {
        if (skuIds == null || skuIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // DTO转map，key为skuId，value为总可用库存
        Map<Long, Long> stockMap = wareSkuMapper.getSkuIdToStockMap(skuIds).stream()
            .collect(Collectors.toMap(dto -> dto.getSkuId(), dto -> dto.getStock()));
        
        return skuIds.stream()
            .collect(Collectors.toMap(
                Function.identity(),  // 恒等映射 x -> x
                skuId -> stockMap.getOrDefault(skuId, 0L)
            ));
    }

    /**
     * 模拟获取运费
     * @param ReceiverAddressInfo 收货地址信息
     */
    @Override
    public BigDecimal getShippingFee(String ReceiverAddressInfo) {
        long base = ReceiverAddressInfo.length();
        long seed = (base * 423142070409L + 202608301740L) ^ 20041123L;
        long cents = base * CENTS_PER_YUAN + (Math.abs(seed) % 1953L);  // 单位为分

        // 分转为元，保留两位小数
        return BigDecimal.valueOf(cents).divide(BigDecimal.valueOf(CENTS_PER_YUAN), 2, RoundingMode.HALF_UP);
    }
    
}
