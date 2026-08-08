package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsAttrAttrgroupRelation;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 属性&属性分组关联视图对象 pms_attr_attrgroup_relation
 *
 * @author mayao
 * @date 2026-08-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsAttrAttrgroupRelation.class)
public class PmsAttrAttrgroupRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 属性id
     */
    @ExcelProperty(value = "属性id")
    private Long attrId;

    /**
     * 属性分组id
     */
    @ExcelProperty(value = "属性分组id")
    private Long attrGroupId;

    /**
     * 属性组内排序
     */
    @ExcelProperty(value = "属性组内排序")
    private Long attrSort;

    // ======== 联表冗余字段（非 gen，手动维护） ========

    /**
     * 属性名（联查 pms_attr）
     */
    private String attrName;

    /**
     * 属性分组名（联查 pms_attr_group）
     */
    private String attrGroupName;


}
