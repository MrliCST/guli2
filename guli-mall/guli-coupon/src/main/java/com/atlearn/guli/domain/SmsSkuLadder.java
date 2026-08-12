package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品阶梯价格对象 sms_sku_ladder
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sms_sku_ladder")
public class SmsSkuLadder extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
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
