package com.atlearn.guli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 商品展示线程池配置
 *
 * <p>用于商品详情页的异步聚合查询，将 sku 信息、图片、销售属性、spu 介绍、
 * 基本参数族等多源查询并行化，缩短整体响应时间。</p>
 *
 * @author mayao
 * @date 2026-08-17
 */
@EnableAsync
@Configuration
public class AsyncDisplayThreadConfig {

    @Bean("displayExecutor")
    public Executor displayExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("display-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
