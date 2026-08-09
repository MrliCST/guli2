package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsBrand;
import com.atlearn.guli.validate.ListValCheck;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.hibernate.validator.constraints.URL;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 品牌业务对象 pms_brand
 *
 * @author mayao
 * @date 2026-07-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsBrand.class, reverseConvertGenerate = false)
public class PmsBrandBo extends BaseEntity {

    /**
     * 品牌id
     */
    @NotNull(message = "品牌id不能为空", groups = { EditGroup.class })
    private Long brandId;

    /**
     * 品牌名
     */
    private String name;

    /**
     * 品牌logo地址
     */
    @NotBlank(message = "Logo地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ListValCheck(vals = {0, 1}, message = "显示状态只能为0或1", groups = { AddGroup.class, EditGroup.class })
    private Long showStatus;

    /**
     * 检索首字母（A-Z）
     */
    @Pattern(regexp = "^[A-Z]$", message = "检索首字母必须为单个大写字母A-Z", groups = { AddGroup.class, EditGroup.class })
    private String firstLetter;

    /**
     * 排序
     */
    private Long sort;


}
