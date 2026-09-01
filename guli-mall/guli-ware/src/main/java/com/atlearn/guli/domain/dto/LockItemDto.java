package com.atlearn.guli.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存锁定/解锁明细 DTO
 * 封装为 orderDetail实体太重，作为批量CAS锁库和解库操作中，service -> mapper 的中间者
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockItemDto {

    /** 仓库id */
    private Long wareId;

    /** 商品skuid */
    private Long skuId;

    /** 商品名称 */
    private String skuName;

    /** 锁定/解锁数量 */
    private Integer lockNum;
}
