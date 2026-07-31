package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsCategory;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 商品三级分类视图对象 pms_category
 *
 * @author mayao
 * @date 2026-07-25
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsCategory.class)
public class PmsCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @ExcelProperty(value = "分类id")
    private Long catId;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String name;

    /**
     * 父分类id
     */
    @ExcelProperty(value = "父分类id")
    private Long parentCid;

    /**
     * 层级
     */
    @ExcelProperty(value = "层级")
    private Long catLevel;

    /**
     * 是否显示[0-不显示，1显示]
     */
    @ExcelProperty(value = "是否显示[0-不显示，1显示]")
    private Long showStatus;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 图标地址
     */
    @ExcelProperty(value = "图标地址")
    private String icon;

    /**
     * 计量单位
     */
    @ExcelProperty(value = "计量单位")
    private String productUnit;

    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量")
    private Long productCount;

    /**
     * 子分类
     */
    private List<PmsCategoryVo> children = new ArrayList<>();
}
