package com.atlearn.guli.core;

/**
 * 用户信息 ThreadLocal 持有者
 * <p>
 * 由 {@link com.atlearn.guli.interceptor.UserInfoInterceptor} 在请求开始时填充，
 * 请求结束时移除。
 *
 * @author guli
 */
public final class UserInfoContext {

    private static final ThreadLocal<UserInfo> CONTEXT = new ThreadLocal<>();

    private UserInfoContext() {
    }

    /** 设置用户信息 */
    public static void set(UserInfo userInfo) {
        CONTEXT.set(userInfo);
    }

    /** 获取用户信息 */
    public static UserInfo get() {
        return CONTEXT.get();
    }

    /** 获取用户 ID */
    public static Long getUserId() {
        UserInfo info = CONTEXT.get();
        return info == null ? null : info.getUserId();
    }

    /** 获取用户标识 */
    public static String getUserKey() {
        UserInfo info = CONTEXT.get();
        return info == null ? null : info.getUserKey();
    }

    /** 是否为临时用户 */
    public static boolean isTempUser() {
        UserInfo info = CONTEXT.get();
        return info != null && Boolean.TRUE.equals(info.getIsTempUser());
    }

    /** 移除 ThreadLocal */
    public static void remove() {
        CONTEXT.remove();
    }

}
