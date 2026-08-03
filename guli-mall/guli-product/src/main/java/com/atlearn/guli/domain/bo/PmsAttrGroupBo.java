package com.atlearn.guli.domain.bo;

import com.atlearn.guli.domain.PmsAttrGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 属性分组业务对象 pms_attr_group
 *
 * @author mayao
 * @date 2026-08-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PmsAttrGroup.class, reverseConvertGenerate = false)
public class PmsAttrGroupBo extends BaseEntity {

    /**
     * 分组id
     */
    @NotNull(message = "分组id不能为空", groups = { EditGroup.class })
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
