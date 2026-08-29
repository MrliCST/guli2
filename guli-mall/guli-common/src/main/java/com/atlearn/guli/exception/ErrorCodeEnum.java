package com.atlearn.guli.exception;

import lombok.Getter;

/**
 * 业务错误码枚举
 *
 * @author mayao
 * @date 2026-08-03
 */
@Getter
public enum ErrorCodeEnum {

    /** 参数校验失败 */
    VALIDATION_FAILED(10001, "参数校验失败"),

    ASYNC_READ_SKUITEM_FAILED(10002, "异步读取sku信息失败"),

    /** 验证码已过期或不存在 */
    CODE_EXPIRED(10003, "验证码已过期，请重新获取"),

    /** 验证码错误 */
    CODE_INVALID(10004, "验证码错误"),

    /** 用户名已存在 */
    USERNAME_EXISTS(10005, "用户名已存在"),

    /** 邮箱已存在 */
    EMAIL_EXISTS(10006, "邮箱已存在"),

    /** 用户不存在 */
    USER_NOT_FOUND(10007, "用户不存在"),

    /** 密码错误 */
    PASSWORD_ERROR(10008, "密码错误"),

    /** 账号已被禁用 */
    ACCOUNT_DISABLED(10009, "账号已被禁用"),

    /** 不支持的授权类型 */
    UNSUPPORTED_GRANT_TYPE(10010, "不支持的授权类型"),

    /** 未登录 */
    UNAUTHORIZED(10011, "未登录，请先登录"),

    /** 未知错误 */
    UNKNOWN_ERROR(99999, "未知错误");

    private final int code;
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
