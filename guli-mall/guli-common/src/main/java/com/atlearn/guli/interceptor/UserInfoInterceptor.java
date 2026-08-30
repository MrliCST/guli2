package com.atlearn.guli.interceptor;

import com.atlearn.guli.constant.AuthConstant;
import com.atlearn.guli.core.UserInfo;
import com.atlearn.guli.core.UserInfoContext;
import com.atlearn.guli.exception.BusinessException;
import com.atlearn.guli.exception.ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户信息拦截器
 * <p>
 * 统一登录验证：从请求头取出 token，去 Redis 拿用户信息，存入 ThreadLocal。
 * 没有 token 或 Redis 中不存在时，抛出未登录异常，请求被拦截。
 * 请求结束时移除 ThreadLocal，防止内存泄漏。
 *
 * @author guli
 */
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    /** Authorization header 中 token 的前缀 */
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        // 请求头的token，换redis的userInfo，设置进入ThreadLocal
        String token = authHeader.substring(TOKEN_PREFIX.length());
        UserInfo userInfo = RedisUtils.getCacheObject(AuthConstant.AUTH_TOKEN_KEY_PREFIX + token);
        if (userInfo == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        userInfo.setUserKey(token);
        UserInfoContext.set(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserInfoContext.remove();
    }

}
