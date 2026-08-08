package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsAttrAttrgroupRelation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 属性&属性分组关联业务对象 pms_attr_attrgroup_relation
 *
 * @author mayao
 * @date 2026-08-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsAttrAttrgroupRelation.class, reverseConvertGenerate = false)
public class PmsAttrAttrgroupRelationBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
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
