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
    private BigDecimal payRefenceAmount;  // 确认订单业务计算的参考价格，待验价
    private String note;
}
