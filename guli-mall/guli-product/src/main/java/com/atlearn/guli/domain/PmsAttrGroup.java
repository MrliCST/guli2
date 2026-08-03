package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 属性分组对象 pms_attr_group
 *
 * @author mayao
 * @date 2026-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_attr_group")
public class PmsAttrGroup extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    @TableId(value = "attr_group_id")
    private Long attrGroupId;

    /**
     * 组名
     */
    private String attrGroupName;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 描述
     */
    private String descript;

    /**
     * 组图标
     */
    private String icon;

    /**
     * 所属分类id
     */
    private Long catelogId;


}
