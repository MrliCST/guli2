package com.atlearn.guli.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 品牌分类关联对象 pms_category_brand_relation
 *
 * @author mayao
 * @date 2026-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_category_brand_relation")
public class PmsCategoryBrandRelation extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 分类id
     */
    private Long catelogId;

    /**
     * 
     */
    private String brandName;

    /**
     * 
     */
    private String catelogName;


}
