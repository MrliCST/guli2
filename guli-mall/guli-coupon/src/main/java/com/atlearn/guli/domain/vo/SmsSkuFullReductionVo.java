package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.SmsSkuFullReduction;
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
 * 商品满减信息视图对象 sms_sku_full_reduction
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SmsSkuFullReduction.class)
public class SmsSkuFullReductionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * spu_id
     */
    @ExcelProperty(value = "spu_id")
    private Long skuId;

    /**
     * 满多少
     */
    @ExcelProperty(value = "满多少")
    private Long fullPrice;

    /**
     * 减多少
     */
    @ExcelProperty(value = "减多少")
    private Long reducePrice;

    /**
     * 是否参与其他优惠
     */
    @ExcelProperty(value = "是否参与其他优惠")
    private Long addOther;


}
