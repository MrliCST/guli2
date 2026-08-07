package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsCategoryBrandRelation;
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
 * 品牌分类关联视图对象 pms_category_brand_relation
 *
 * @author mayao
 * @date 2026-08-06
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PmsCategoryBrandRelation.class)
public class PmsCategoryBrandRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 品牌id
     */
    @ExcelProperty(value = "品牌id")
    private Long brandId;

    /**
     * 分类id
     */
    @ExcelProperty(value = "分类id")
    private Long catelogId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String brandName;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String catelogName;


}
