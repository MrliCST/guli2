package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.SmsSkuFullReduction;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品满减信息业务对象 sms_sku_full_reduction
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SmsSkuFullReduction.class, reverseConvertGenerate = false)
public class SmsSkuFullReductionBo extends BaseEntity {

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
