package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.WmsWareSku;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品库存业务对象 wms_ware_sku
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsWareSku.class, reverseConvertGenerate = false)
public class WmsWareSkuBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 库存数
     */
    private Long stock;

    /**
     * sku_name
     */
    private String skuName;

    /**
     * 锁定库存
     */
    private Long stockLocked;


}
