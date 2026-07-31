package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品三级分类对象 pms_category
 *
 * @author mayao
 * @date 2026-07-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_category")
public class PmsCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId(value = "cat_id")
    private Long catId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id
     */
    private Long parentCid;

    /**
     * 层级
     */
    private Long catLevel;

    /**
     * 是否显示[0-不显示，1显示]（逻辑删除字段：1=未删除，0=已删除）
     */
    @TableLogic(value = "1", delval = "0")
    private Long showStatus;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 图标地址
     */
    private String icon;

    /**
     * 计量单位
     */
    private String productUnit;

    /**
     * 商品数量
     */
    private Long productCount;


}
