package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * ==> 用于远程服务的参数
 * 商品spu积分设置远程业务对象 sms_spu_bounds
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeSpuBoundsBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     *
     */
    private Long spuId;

    /**
     * 成长积分
     */
    private Long growBounds;

    /**
     * 购物积分
     */
    private Long buyBounds;

    /**
     * 优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]
     */
    private Long work;

}
