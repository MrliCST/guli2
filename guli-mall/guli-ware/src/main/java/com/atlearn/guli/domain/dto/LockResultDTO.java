package com.atlearn.guli.domain.dto;

import lombok.Data;

/**
 * 库存锁定结果
 *
 * @author guli
 */
@Data
public class LockResultDTO {

    private Long skuId;

    private Integer success;

}
