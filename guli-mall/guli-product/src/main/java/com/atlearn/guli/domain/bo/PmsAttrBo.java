package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsAttr;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品属性业务对象 pms_attr
 *
 * @author mayao
 * @date 2026-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsAttr.class, reverseConvertGenerate = false)
public class PmsAttrBo extends BaseEntity {

    /**
     * 属性id
     */
    @NotNull(message = "属性id不能为空", groups = { EditGroup.class })
    private Long attrId;

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Long searchType;

    /**
     * 值类型[0-为单个值，1-可以选择多个值]
     */
    private Long valueType;

    /**
     * 属性图标
     */
    private String icon;

    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;

    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Long attrType;

    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;

    /**
     * 所属分类
     */
    private Long catelogId;

    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Long showDesc;


}
