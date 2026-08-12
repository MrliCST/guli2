package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品满减信息对象 sms_sku_full_reduction
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sms_sku_full_reduction")
public class SmsSkuFullReduction extends BaseEntity {

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
     * 满多少
     */
    private Long fullPrice;

    /**
     * 减多少
     */
    private Long reducePrice;

    /**
     * 是否参与其他优惠
     */
    private Long addOther;


}
