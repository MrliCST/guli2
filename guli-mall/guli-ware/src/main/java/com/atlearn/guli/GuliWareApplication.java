package com.atlearn.guli;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 仓储服务
 *
 * @author guli
 */
@EnableDubbo
@SpringBootApplication
public class GuliWareApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GuliWareApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  仓储服务启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
