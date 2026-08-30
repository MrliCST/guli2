package com.atlearn.guli.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车项远程 VO
 *
 * @author guli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeCartItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * sku id
     */
    private Long skuId;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 默认图片
     */
    private String defaultImage;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private Integer count;

    /**
     * 商品销售属性
     */
    private List<RmeSkuSaleAttrValueVo> saleAttr;

}
