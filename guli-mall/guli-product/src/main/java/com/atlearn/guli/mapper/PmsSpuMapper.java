package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.PmsSpuInfo;
import com.atlearn.guli.domain.vo.PmsAttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SPU信息Mapper接口
 *
 * @author mayao
 * @date 2026-08-08
 */
public interface PmsSpuMapper extends BaseMapper<PmsSpuInfo> {

    /**
     * 根据分类id查询属性分组及其属性列表
     */
    List<PmsAttrGroupWithAttrsVo> selectAttrGroupsWithAttrs(@Param("catelogId") Long catelogId);
}
