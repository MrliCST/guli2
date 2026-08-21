package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.PmsProductAttrValue;
import com.atlearn.guli.domain.vo.PmsSkuItemVo;
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

    /**
     * 四表联查：查询spu的基本参数族（以分组聚合，每组下多个基本属性）
     *
     * @param spuId     商品id（主条件）
     * @param catelogId 所属分类id（辅助条件）
     * @return 基本参数族列表
     */
    List<PmsSkuItemVo.SpuBaseAttrGroup> selectBaseAttrGroupBySpuId(@Param("spuId") Long spuId, @Param("catelogId") Long catelogId);

}
