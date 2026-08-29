package com.atlearn.guli.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 密码登录请求体
 *
 * @author guli
 */
@Data
public class PasswordLoginBody implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 30, message = "用户名长度必须在2-30之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 5, max = 30, message = "密码长度必须在5-30之间")
    private String password;

}
