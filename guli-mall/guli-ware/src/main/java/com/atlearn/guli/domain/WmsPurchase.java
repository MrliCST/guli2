package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 采购信息对象 wms_purchase
 *
 * @author mayao
 * @date 2026-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_purchase")
public class WmsPurchase extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 
     */
    private Long assigneeId;

    /**
     * 
     */
    private String assigneeName;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private Long priority;

    /**
     * 
     */
    private Long status;

    /**
     * 
     */
    private Long wareId;

    /**
     * 
     */
    private Long amount;


}
