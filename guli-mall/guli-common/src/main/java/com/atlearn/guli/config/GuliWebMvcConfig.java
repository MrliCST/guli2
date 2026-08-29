package com.atlearn.guli.config;

import com.atlearn.guli.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * <p>
 * 注册 {@link UserInfoInterceptor}，拦截所有请求。
 *
 * @author guli
 */
@Configuration
public class GuliWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor())
            .addPathPatterns("/**");
    }

}
