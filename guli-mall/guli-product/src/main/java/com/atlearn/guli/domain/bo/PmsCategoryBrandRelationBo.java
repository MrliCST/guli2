package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsCategoryBrandRelation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 品牌分类关联业务对象 pms_category_brand_relation
 *
 * @author mayao
 * @date 2026-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsCategoryBrandRelation.class, reverseConvertGenerate = false)
public class PmsCategoryBrandRelationBo extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
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
