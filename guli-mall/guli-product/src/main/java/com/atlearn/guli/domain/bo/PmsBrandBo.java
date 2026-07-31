package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsBrand;
import org.dromara.common.mybatis.core.domain.BaseEntity;
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
    private String logo;

    /**
     * 介绍
     */
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Long showStatus;

    /**
     * 检索首字母
     */
    private String firstLetter;

    /**
     * 排序
     */
    private Long sort;


}
