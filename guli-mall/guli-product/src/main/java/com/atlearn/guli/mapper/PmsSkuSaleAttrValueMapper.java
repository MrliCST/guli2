package com.atlearn.guli.mapper;

import com.atlearn.guli.domain.PmsSkuSaleAttrValue;
import com.atlearn.guli.domain.vo.PmsSkuItemVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * sku销售属性&值Mapper接口
 *
 * @author mayao
 * @date 2026-08-09
 */
public interface PmsSkuSaleAttrValueMapper extends BaseMapperPlus<PmsSkuSaleAttrValue, PmsSkuSaleAttrValue> {

    /**
     * 按skuId查询销售属性组合（以 attr_id 分组，聚合属性值）
     *
     * @param skuId skuId
     * @return 销售属性组合列表
     */
    List<PmsSkuItemVo.SkuItemSaleAttr> selectSaleAttrsBySpuId(@Param("spuId") Long spuId);

}
