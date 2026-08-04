package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsAttr;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 商品属性视图对象 pms_attr
 *
 * @author mayao
 * @date 2026-08-03
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsAttr.class)
public class PmsAttrVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    @ExcelProperty(value = "属性id")
    private Long attrId;

    /**
     * 属性名
     */
    @ExcelProperty(value = "属性名")
    private String attrName;

    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    @ExcelProperty(value = "是否需要检索[0-不需要，1-需要]")
    private Long searchType;

    /**
     * 值类型[0-为单个值，1-可以选择多个值]
     */
    @ExcelProperty(value = "值类型[0-为单个值，1-可以选择多个值]")
    private Long valueType;

    /**
     * 属性图标
     */
    @ExcelProperty(value = "属性图标")
    private String icon;

    /**
     * 可选值列表[用逗号分隔]
     */
    @ExcelProperty(value = "可选值列表[用逗号分隔]")
    private String valueSelect;

    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    @ExcelProperty(value = "属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]")
    private Long attrType;

    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    @ExcelProperty(value = "启用状态[0 - 禁用，1 - 启用]")
    private Long enable;

    /**
     * 所属分类
     */
    @ExcelProperty(value = "所属分类")
    private Long catelogId;

    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    @ExcelProperty(value = "快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整")
    private Long showDesc;


}
