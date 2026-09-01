package com.atlearn.guli.domain.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * sku 可用库存统计视图对象
 * 封装为实体太重，临时接一下mapper的返回值，之后转为 map 传递
 *
 * @author mayao
 * @date 2026-08-15
 */
@Data
public class SkuIdToStockDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * 可用库存（库存数 - 锁定库存）
     */
    private Long stock;

}
