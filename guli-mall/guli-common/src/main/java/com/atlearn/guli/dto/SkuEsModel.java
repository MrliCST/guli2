package com.atlearn.guli.dto;

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

/* 
    索引创建语句，模型结构

    PUT /sku_index
    {
        "settings": {
            "number_of_shards": 1,
            "number_of_replicas": 0
        },
        "mappings": {
            "properties": {
                "skuId": {
                    "type": "long"
                },
                "spuId": {
                    "type": "long"
                },
                "skuTitle": {
                    "type": "text",
                    "analyzer": "ik_max_word",
                    "search_analyzer": "ik_smart"
                },
                "skuPrice": {
                    "type": "scaled_float",
                    "scaling_factor": 100
                },
                "skuImg": {
                    "type": "keyword",
                    "index": false
                },
                "saleCount": {
                    "type": "long"
                },
                "hasStock": {
                    "type": "boolean"
                },
                "hotScore": {
                    "type": "long"
                },
                "brandId": {
                    "type": "long"
                },
                "catalogId": {
                    "type": "long"
                },
                "brandName": {
                    "type": "keyword"
                },
                "brandImg": {
                    "type": "keyword",
                    "index": false
                },
                "catalogName": {
                    "type": "keyword"
                },
                "attrs": {
                    "type": "nested",
                    "properties": {
                        "attrId": {
                            "type": "long"
                        },
                        "attrName": {
                            "type": "keyword"
                        },
                        "attrValue": {
                            "type": "keyword"
                        }
                    }
                }
            }
        }
    }
*/