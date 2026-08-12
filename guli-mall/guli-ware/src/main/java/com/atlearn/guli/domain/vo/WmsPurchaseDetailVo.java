package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsPurchaseDetail;
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
 * 采购单详情视图对象 wms_purchase_detail
 *
 * @author mayao
 * @date 2026-08-13
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsPurchaseDetail.class)
public class WmsPurchaseDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 采购单id
     */
    @ExcelProperty(value = "采购单id")
    private Long purchaseId;

    /**
     * 采购商品id
     */
    @ExcelProperty(value = "采购商品id")
    private Long skuId;

    /**
     * 采购数量
     */
    @ExcelProperty(value = "采购数量")
    private Long skuNum;

    /**
     * 采购金额
     */
    @ExcelProperty(value = "采购金额")
    private Long skuPrice;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 状态[0新建，1已分配，2正在采购，3已完成，4采购失败]
     */
    @ExcelProperty(value = "状态[0新建，1已分配，2正在采购，3已完成，4采购失败]")
    private Long status;


}
