package com.atlearn.guli.dto;

import lombok.Data;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;

import java.math.BigDecimal;
import java.util.List;

/**
 * SKU 检索文档模型（对应 ES 索引 sku_index）
 *
 * @author mayao
 * @date 2026-08-14
 */
@Data
@IndexName("sku_index")
public class SkuEsModel {

    /**
     * 文档主键（ES 的 _id）
     */
    @IndexId
    private Long skuId;

    private Long spuId;

    /**
     * 检索标题：分词检索，中文用 IK 分词器
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_SMART)
    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount; //销量

    private Boolean hasStock; //是否有库存

    private Long hotScore; //热度评分

    private Long brandId;

    private Long catalogId;

    /**
     * 品牌名：精确匹配/聚合，用 KEYWORD 不分词
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    private String brandName;

    private String brandImg;

    /**
     * 分类名：精确匹配/聚合
     */
    @IndexField(fieldType = FieldType.KEYWORD)
    private String catalogName;

    /**
     * 检索属性：嵌套类型（支持对象列表）
     */
    @IndexField(fieldType = FieldType.NESTED)
    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
