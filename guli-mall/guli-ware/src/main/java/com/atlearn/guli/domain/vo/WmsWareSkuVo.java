package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsWareSku;
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
 * 商品库存视图对象 wms_ware_sku
 *
 * @author mayao
 * @date 2026-08-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsWareSku.class)
public class WmsWareSkuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * sku_id
     */
    @ExcelProperty(value = "sku_id")
    private Long skuId;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long wareId;

    /**
     * 库存数
     */
    @ExcelProperty(value = "库存数")
    private Long stock;

    /**
     * sku_name
     */
    @ExcelProperty(value = "sku_name")
    private String skuName;

    /**
     * 锁定库存
     */
    @ExcelProperty(value = "锁定库存")
    private Long stockLocked;


}
