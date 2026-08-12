package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.SmsSpuBounds;
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
 * 商品spu积分设置视图对象 sms_spu_bounds
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SmsSpuBounds.class)
public class SmsSpuBoundsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long spuId;

    /**
     * 成长积分
     */
    @ExcelProperty(value = "成长积分")
    private Long growBounds;

    /**
     * 购物积分
     */
    @ExcelProperty(value = "购物积分")
    private Long buyBounds;

    /**
     * 优惠生效情况[1111（四个状态位，从右到左）;0 - 无优惠，成长积分是否赠送;1 - 无优惠，购物积分是否赠送;2 - 有优惠，成长积分是否赠送;3 - 有优惠，购物积分是否赠送【状态位0：不赠送，1：赠送】]
     */
    @ExcelProperty(value = "优惠生效情况[1111", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "四=个状态位，从右到左")
    private Long work;


}
