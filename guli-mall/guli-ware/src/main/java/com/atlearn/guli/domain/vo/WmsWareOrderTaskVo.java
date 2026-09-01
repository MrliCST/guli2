package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.WmsWareOrderTask;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 库存工作单视图对象 wms_ware_order_task
 *
 * @author guli
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WmsWareOrderTask.class)
public class WmsWareOrderTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "订单id")
    private Long orderId;

    @ExcelProperty(value = "订单号")
    private String orderSn;

    @ExcelProperty(value = "收货人")
    private String consignee;

    @ExcelProperty(value = "收货人电话")
    private String consigneeTel;

    @ExcelProperty(value = "配送地址")
    private String deliveryAddress;

    @ExcelProperty(value = "订单备注")
    private String orderComment;

    @ExcelProperty(value = "付款方式")
    private Integer paymentWay;

    @ExcelProperty(value = "任务状态")
    private Integer taskStatus;

    @ExcelProperty(value = "订单描述")
    private String orderBody;

    @ExcelProperty(value = "物流单号")
    private String trackingNo;

    @ExcelProperty(value = "工作单备注")
    private String taskComment;

}
