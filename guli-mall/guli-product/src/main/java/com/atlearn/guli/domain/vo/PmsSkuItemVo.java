package com.atlearn.guli.domain.vo;

import com.atlearn.guli.domain.PmsSkuImages;
import com.atlearn.guli.domain.PmsSkuInfo;
import com.atlearn.guli.domain.PmsSpuInfoDesc;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * sku详情 VO
 *
 * @author mayao
 * @date 2026-08-17
 */
@Data
public class PmsSkuItemVo {

    /**
     * sku信息
     */
    private PmsSkuInfo skuInfo;

    /**
     * sku图片信息
     */
    private List<PmsSkuImages> skuImages;

    /**
     * spu销售属性组合
     */
    private List<SkuItemSaleAttr> skuItemSaleAttr;

    /**
     * spu的介绍
     */
    private PmsSpuInfoDesc spuInfoDesc;

    /**
     * spu基本参数族
     */
    private List<SpuBaseAttrGroup> spuBaseAttrGroup;

    /**
     * spu销售属性
     */
    @Data
    public static class SkuItemSaleAttr {

        /**
         * 属性id
         */
        private Long attrId;

        /**
         * 属性名
         */
        private String attrName;

        /**
         * 属性值集合
         */
        private List<String> attrValues;
    }

    /**
     * spu基本属性分组
     */
    @Data
    public static class SpuBaseAttrGroup {

        /**
         * 分组名
         */
        private String groupName;

        /**
         * 分组下的属性集合
         */
        private List<SpuBaseAttr> attrs;
    }

    /**
     * spu基本属性
     */
    @Data
    public static class SpuBaseAttr {

        /**
         * 属性名
         */
        private String attrName;

        /**
         * 属性值
         */
        private String attrValue;
    }
}
