package com.atlearn.guli.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 库存锁定结果 VO
 *
 * @author guli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeWareStockLockResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 是否锁定成功 */
    private Boolean success;

    /** 提示信息 */
    private String message;

    public static RmeWareStockLockResultVo ok() {
        return RmeWareStockLockResultVo.builder().success(true).message("锁定成功").build();
    }

    public static RmeWareStockLockResultVo fail(String message) {
        return RmeWareStockLockResultVo.builder().success(false).message(message).build();
    }
}
