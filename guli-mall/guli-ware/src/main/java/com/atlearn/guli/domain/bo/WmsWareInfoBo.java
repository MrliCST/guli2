package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.WmsWareInfo;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 仓库信息业务对象 wms_ware_info
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WmsWareInfo.class, reverseConvertGenerate = false)
public class WmsWareInfoBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 仓库名
     */
    private String name;

    /**
     * 仓库地址
     */
    private String address;

    /**
     * 区域编码
     */
    private String areacode;


}
