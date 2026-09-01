package com.atlearn.guli.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 消息序列化配置
 * <p>
 * 仅当 classpath 上存在 AMQP 相关类时才生效（guli-common 中 spring-boot-starter-amqp 为 optional）
 *
 * @author guli
 */
@Configuration
@ConditionalOnClass(MessageConverter.class)
public class GuliRabbitMqConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
