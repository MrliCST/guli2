package com.atlearn.guli.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性分组 & 其下属性值储列表 VO
 *
 * @author mayao
 * @date 2026-08-09
 */
@Data
public class PmsAttrGroupWithAttrsVo implements Serializable {

    private Long attrGroupId;

    private String attrGroupName;

    private Long sort;

    private String descript;

    private String icon;

    private Long catelogId;

    /**
     * 该分组下的属性列表
     */
    private List<PmsAttrVo> attrs;
}
