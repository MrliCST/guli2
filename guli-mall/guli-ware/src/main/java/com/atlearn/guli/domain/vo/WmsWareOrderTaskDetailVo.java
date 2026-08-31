package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsWareOrderTaskDetail;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 库存工作单详情视图对象 wms_ware_order_task_detail
 *
 * @author guli
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsWareOrderTaskDetail.class)
public class WmsWareOrderTaskDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "sku_id")
    private Long skuId;

    @ExcelProperty(value = "sku_name")
    private String skuName;

    @ExcelProperty(value = "购买个数")
    private Integer skuNum;

    @ExcelProperty(value = "工作单id")
    private Long taskId;

    @ExcelProperty(value = "仓库id")
    private Long wareId;

    @ExcelProperty(value = "锁定状态")
    private Integer lockStatus;

}
