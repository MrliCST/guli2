package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsBrand;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;



/**
 * 品牌视图对象 pms_brand
 *
 * @author mayao
 * @date 2026-07-30
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsBrand.class)
public class PmsBrandVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @ExcelProperty(value = "品牌id")
    private Long brandId;

    /**
     * 品牌名
     */
    @ExcelProperty(value = "品牌名")
    private String name;

    /**
     * 品牌logo地址
     */
    @ExcelProperty(value = "品牌logo地址")
    private String logo;

    /**
     * 介绍
     */
    @ExcelProperty(value = "介绍")
    private String descript;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    @ExcelProperty(value = "显示状态[0-不显示；1-显示]")
    private Long showStatus;

    /**
     * 检索首字母
     */
    @ExcelProperty(value = "检索首字母")
    private String firstLetter;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;


}
