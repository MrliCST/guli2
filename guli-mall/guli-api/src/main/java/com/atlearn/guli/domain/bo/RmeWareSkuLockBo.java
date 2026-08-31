package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 锁库存请求 BO
 *
 * @author guli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeWareSkuLockBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 订单号 */
    private String orderSn;

    /** 需要锁定的商品列表 */
    private List<LockItem> lockItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LockItem implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /** skuId */
        private Long skuId;

        /** sku名称 */
        private String skuName;

        /** 购买数量 */
        private Integer skuNum;
    }
}
