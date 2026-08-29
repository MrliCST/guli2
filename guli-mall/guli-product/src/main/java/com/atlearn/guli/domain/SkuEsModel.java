package com.atlearn.guli.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IndexName("sku_index")
public class SkuEsModel {

    @IndexId
    private Long skuId;

    private Long spuId;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount; // 销量

    private Boolean hasStock; // 是否有库存

    private Long hotScore; // 热度评分

    private Long brandId;

    private Long catalogId;

    private String brandImg;

    @IndexField(fieldType = FieldType.TEXT)
    private String skuTitle;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String brandName;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String catalogName;

    @IndexField(fieldType = FieldType.NESTED)
    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
