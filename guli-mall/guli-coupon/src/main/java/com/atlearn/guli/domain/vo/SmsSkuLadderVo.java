package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.SmsSkuLadder;
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
 * 商品阶梯价格视图对象 sms_sku_ladder
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SmsSkuLadder.class)
public class SmsSkuLadderVo implements Serializable {

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
     * 满几件
     */
    @ExcelProperty(value = "满几件")
    private Long fullCount;

    /**
     * 打几折
     */
    @ExcelProperty(value = "打几折")
    private Long discount;

    /**
     * 折后价
     */
    @ExcelProperty(value = "折后价")
    private Long price;

    /**
     * 是否叠加其他优惠[0-不可叠加，1-可叠加]
     */
    @ExcelProperty(value = "是否叠加其他优惠[0-不可叠加，1-可叠加]")
    private Long addOther;


}
