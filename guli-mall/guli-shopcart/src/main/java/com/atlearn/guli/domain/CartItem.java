package com.atlearn.guli.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.atlearn.guli.domain.vo.RmeSkuSaleAttrValueVo;

/**
 * 购物车项
 *
 * @author guli
 */
@Data
public class CartItem implements Serializable {

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

    /**
     * 是否有库存
     */
    private Boolean hasStock;

    /**
     * 小计金额 = 单价 × 数量
     */
    public BigDecimal getTotalPrice() {
        if (price == null || count == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(count));
    }

}
