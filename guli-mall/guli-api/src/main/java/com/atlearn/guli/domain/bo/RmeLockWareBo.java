package com.atlearn.guli.domain.bo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RmeLockWareBo {
    /**
     * 例如 skuid: 10 的商品，需要锁定10个。
     * 现在仓库有 : {
     *      "ware1" : 5,
     *      "ware2" : 4,
     *      "ware3" : 3
     *      "wate4" : 2
     * }
     * 
     * 那么这个 RmeLockWareBo 应该为: {
     *      "skuId" : 10,  
     *      "wareDistribute" : [
     *          { "wareId" : 1, "lockNum": 5 }
     *          { "wareId" : 2, "lockNum": 4 }
     *          { "wareId" : 3, "lockNum": 1 }
     *      ]
     * }
     */

    /**
     * 需要锁定的商品id
     */
    private Long skuId;

    /**
     * 需要锁定的商品名称
     */
    private String skuName;

    /**
     * 分配的锁定信息
     */
    private List<WareDistribute> wareDistribute;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WareDistribute {
        /**
         * 仓库id
         */
        private Long wareId;
        
        /**
         * 分配到的锁定数
         */
        private Integer lockNum;
    }
}
