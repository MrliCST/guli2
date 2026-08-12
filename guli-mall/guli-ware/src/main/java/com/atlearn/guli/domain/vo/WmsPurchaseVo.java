package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsPurchase;
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
 * 采购信息视图对象 wms_purchase
 *
 * @author mayao
 * @date 2026-08-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsPurchase.class)
public class WmsPurchaseVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long assigneeId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String assigneeName;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private String phone;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long priority;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long status;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long wareId;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long amount;


}
