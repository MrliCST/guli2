package com.atlearn.guli.dubbo;

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
    
}
