package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.PmsAttrAttrgroupRelation;
import com.atlearn.guli.domain.vo.PmsAttrAttrgroupRelationVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;
import java.util.Set;

/**
 * 属性&属性分组关联Mapper接口
 *
 * @author mayao
 * @date 2026-08-08
 */
public interface PmsAttrAttrgroupRelationMapper extends BaseMapperPlus<PmsAttrAttrgroupRelation, PmsAttrAttrgroupRelationVo> {

    /**
     * 根据属性分组id查询已关联的属性id集合
     */
    Set<Long> selectUsedAttrIdsByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 联表查询关联列表（附带属性名和分组名）
     */
    List<PmsAttrAttrgroupRelationVo> selectVoListByGroupId(@Param("attrGroupId") Long attrGroupId);

}
