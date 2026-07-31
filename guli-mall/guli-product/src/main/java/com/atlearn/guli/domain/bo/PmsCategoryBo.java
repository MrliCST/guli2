package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsCategory;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 商品三级分类业务对象 pms_category
 *
 * @author mayao
 * @date 2026-07-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsCategory.class, reverseConvertGenerate = false)
public class PmsCategoryBo extends BaseEntity {

    /**
     * 分类id
     */
    @NotNull(message = "分类id不能为空", groups = { EditGroup.class })
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
     * 是否显示[0-不显示，1显示]
     */
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
