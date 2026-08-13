package com.atlearn.guli.config;

import org.dromara.easyes.spring.annotation.EsMapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * guli ES Mapper 扫描配置
 *
 * <p>RuoYi 自带的 EasyEsConfiguration 只扫描 {@code org.dromara.**.esmapper}，
 * 而 guli 的包是 {@code com.atlearn.guli}，因此需要单独声明扫描路径。</p>
 *
 * @author mayao
 * @date 2026-08-14
 */
@Configuration
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
@EsMapperScan("com.atlearn.guli.**.esmapper")
public class GuliEsConfiguration {
}
