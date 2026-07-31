package com.atlearn.guli;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 优惠券服务
 *
 * @author guli
 */
@EnableDubbo
@SpringBootApplication
public class GuliCouponApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GuliCouponApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  优惠券服务启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
