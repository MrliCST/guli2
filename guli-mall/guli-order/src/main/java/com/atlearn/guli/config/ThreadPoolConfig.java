package com.atlearn.guli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author guli
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor executor() {
        return new ThreadPoolExecutor(
            20,
            200,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

}
