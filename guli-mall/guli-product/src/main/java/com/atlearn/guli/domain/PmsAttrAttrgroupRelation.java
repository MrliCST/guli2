package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 属性&属性分组关联对象 pms_attr_attrgroup_relation
 *
 * @author mayao
 * @date 2026-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_attr_attrgroup_relation")
public class PmsAttrAttrgroupRelation extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 属性id
     */
    private Long attrId;

    /**
     * 属性分组id
     */
    private Long attrGroupId;

    /**
     * 属性组内排序
     */
    private Long attrSort;


}
