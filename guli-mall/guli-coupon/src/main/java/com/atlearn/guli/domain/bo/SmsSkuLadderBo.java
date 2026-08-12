package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.SmsSkuLadder;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品阶梯价格业务对象 sms_sku_ladder
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SmsSkuLadder.class, reverseConvertGenerate = false)
public class SmsSkuLadderBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
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
