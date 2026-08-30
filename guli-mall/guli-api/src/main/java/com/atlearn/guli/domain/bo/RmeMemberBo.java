package com.atlearn.guli.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员远程 BO（入参对象，字段精简）
 *
 * @author guli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RmeMemberBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

}
