package com.atlearn.guli.constant;

import java.time.Duration;

/**
 * 认证相关常量
 *
 * @author guli
 */
public class AuthConstant {

    private AuthConstant() {
    }

    /** Redis 验证码 key 前缀 */
    public static final String CODE_KEY_PREFIX = "mail:code:";

    /** 验证码有效期 */
    public static final Duration CODE_TTL = Duration.ofMinutes(5);

    /** 验证码长度 */
    public static final int CODE_LENGTH = 6;

    /** 邮件主题 */
    public static final String MAIL_SUBJECT = "谷粒商城-验证码";

    /** 邮件正文模板 */
    public static final String MAIL_CONTENT_TEMPLATE = "您的验证码为：%s，有效时间5分钟，请勿泄露给他人。";

    /** 授权类型：密码登录 */
    public static final String GRANT_TYPE_PASSWORD = "password";

    /** 授权类型：邮箱登录 */
    public static final String GRANT_TYPE_EMAIL = "email";

    /** 会员用户类型标识 */
    public static final String USER_TYPE_MEMBER = "member";

    /** 登录后用户信息 Redis key 前缀 */
    public static final String AUTH_TOKEN_KEY_PREFIX = "guli:auth:";

}
