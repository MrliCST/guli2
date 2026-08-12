package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.WmsPurchaseDetail;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 采购单详情业务对象 wms_purchase_detail
 *
 * @author mayao
 * @date 2026-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsPurchaseDetail.class, reverseConvertGenerate = false)
public class WmsPurchaseDetailBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 采购单id
     */
    private Long purchaseId;

    /**
     * 采购商品id
     */
    private Long skuId;

    /**
     * 采购数量
     */
    private Long skuNum;

    /**
     * 采购金额
     */
    private Long skuPrice;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
     */
    private Long status;


}
