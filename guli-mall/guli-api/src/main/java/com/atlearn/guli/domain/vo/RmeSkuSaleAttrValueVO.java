package com.atlearn.guli.domain.vo;

import java.io.Serial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ==> 用于远程服务的返回对象
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeSkuSaleAttrValueVo {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * attr_id
     */
    private Long attrId;

    /**
     * 销售属性名
     */
    private String attrName;

    /**
     * 销售属性值
     */
    private String attrValue;

    /**
     * 顺序
     */
    private Long attrSort;
}
