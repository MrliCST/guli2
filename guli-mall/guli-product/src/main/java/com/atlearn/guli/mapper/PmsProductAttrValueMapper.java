package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.PmsProductAttrValue;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * spu属性值Mapper接口
 *
 * @author mayao
 * @date 2026-08-09
 */
public interface PmsProductAttrValueMapper extends BaseMapperPlus<PmsProductAttrValue, PmsProductAttrValue> {

    /**
     * 联表查询spu所属分类下、可检索(search_type=1)的属性值
     *
     * @param spuId     商品id
     * @param catalogId 所属分类id
     * @return 可检索属性值列表
     */
    List<PmsProductAttrValue> selectSearchAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);

}
