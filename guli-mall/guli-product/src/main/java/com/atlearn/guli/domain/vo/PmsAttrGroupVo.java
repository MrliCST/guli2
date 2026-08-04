package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsAttrGroup;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 属性分组视图对象 pms_attr_group
 *
 * @author mayao
 * @date 2026-08-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsAttrGroup.class)
public class PmsAttrGroupVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    @ExcelProperty(value = "分组id")
    private Long attrGroupId;

    /**
     * 组名
     */
    @ExcelProperty(value = "组名")
    private String attrGroupName;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String descript;

    /**
     * 组图标
     */
    @ExcelProperty(value = "组图标")
    private String icon;

    /**
     * 所属分类id
     */
    @ExcelProperty(value = "所属分类id")
    private Long catelogId;


}
