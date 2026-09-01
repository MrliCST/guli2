package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 库存工作单详情对象 wms_ware_order_task_detail
 *
 * @author guli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("wms_ware_order_task_detail")
public class WmsWareOrderTaskDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * sku_name
     */
    private String skuName;

    /**
     * 该仓库被锁定的个数
     */
    private Integer lockNum;

    /**
     * 工作单id
     */
    private Long taskId;

    /**
     * 仓库id
     */
    private Long wareId;

    /**
     * 锁定状态 1-已锁定 2-已解锁 3-扣减
     */
    private Integer lockStatus;

}
