package com.atlearn.guli.domain.bo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitOrderBo {
    private Long memberReceiveAddressId;
    private Integer payType;
    private BigDecimal payAmount;
    private String note;
    /** 防重令牌 */
    private String orderToken;
}
