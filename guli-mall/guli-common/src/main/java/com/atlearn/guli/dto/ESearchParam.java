package com.atlearn.guli.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * ES商品检索参数
 */
@Data
public class ESearchParam {

    /**
     * 全文检索关键词
     */
    private String keyword;

    /**
     * 筛选条件
     */
    private Filter filter;

    /**
     * 多条件排序集合，支持依次多个排序规则
     */
    private List<SortInfo> sortList;

    /**
     * 排序单元信息
     */
    @Data
    public static class SortInfo {
        /**
         * 排序标签：区分排序类型 hot / sales / price
         */
        private String sortTag;

        /**
         * 排序附属参数
         * price价格排序使用，存放区间信息：100_noLimit；热度、销量传空
         */
        private String sortParam;

        /**
         * 排序方向：asc / desc
         */
        private String sortOrder;
    }

    /**
     * 筛选条件
     */
    @Data
    public static class Filter {

        /**
         * 分类
         */
        private String category;

        /**
         * 是否只查询有货商品
         */
        private Boolean hasStock;

        /**
         * 价格区间
         */
        private SkuPriceRange skuPrice;

        /**
         * 品牌id集合
         */
        private List<Long> brandIds;

        /**
         * 属性筛选
         * key：属性标识（system、screen） value：属性值列表
         */
        private Map<String, List<String>> attributes;
    }

    /**
     * 价格区间
     */
    @Data
    public static class SkuPriceRange {
        private String min;
        private String max;
    }
}