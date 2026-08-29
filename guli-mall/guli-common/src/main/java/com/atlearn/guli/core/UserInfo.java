package com.atlearn.guli.core;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户上下文信息
 * <p>
 * 登录用户和临时用户共用此对象，通过 {@link UserInfoContext} 存入 ThreadLocal。
 *
 * @author guli
 */
@Data
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户 ID（临时用户为 null） */
    private Long userId;

    /** 用户唯一标识（登录用户为 token，临时用户为随机 UUID） */
    private String userKey;

    /** 是否为临时用户 */
    private Boolean isTempUser;

}
