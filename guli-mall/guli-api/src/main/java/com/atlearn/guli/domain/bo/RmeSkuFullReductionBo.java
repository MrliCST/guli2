package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ==> 用于远程服务的参数
 * 商品满减信息远程业务对象 sms_sku_full_reduction
 * 满多少，减多少
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeSkuFullReductionBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * spu_id
     */
    private Long skuId;

    /**
     * 满多少
     */
    private BigDecimal fullPrice;

    /**
     * 减多少
     */
    private BigDecimal reducePrice;

    /**
     * 是否参与其他优惠
     */
    private Long addOther;

}
