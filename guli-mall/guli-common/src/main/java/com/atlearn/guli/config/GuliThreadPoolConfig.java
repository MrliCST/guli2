package com.atlearn.guli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author guli
 */
@Configuration
public class GuliThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor executor() {
        int corePoolSize = 20;
        int maximumPoolSize = 200;
        long keepAliveTime = 60;
        int queueCapacity = 10000;
        return new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(queueCapacity),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
