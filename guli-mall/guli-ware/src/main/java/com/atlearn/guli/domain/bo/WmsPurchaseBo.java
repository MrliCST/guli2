package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.WmsPurchase;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 采购信息业务对象 wms_purchase
 *
 * @author mayao
 * @date 2026-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsPurchase.class, reverseConvertGenerate = false)
public class WmsPurchaseBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 
     */
    private Long assigneeId;

    /**
     * 
     */
    private String assigneeName;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Long priority;

    /**
     * 
     */
    private Long status;

    /**
     * 
     */
    private Long wareId;

    /**
     * 
     */
    private Long amount;


}
