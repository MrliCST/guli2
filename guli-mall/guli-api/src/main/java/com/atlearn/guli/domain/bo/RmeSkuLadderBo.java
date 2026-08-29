package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * ==> 用于远程服务的参数
 * 商品阶梯价格远程业务对象 sms_sku_ladder
 * 满几件，打几折
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeSkuLadderBo implements Serializable {

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
     * 满几件
     */
    private Long fullCount;

    /**
     * 打几折
     */
    private Long discount;

    /**
     * 折后价
     */
    private Long price;

    /**
     * 是否叠加其他优惠[0-不可叠加，1-可叠加]
     */
    private Long addOther;

}
