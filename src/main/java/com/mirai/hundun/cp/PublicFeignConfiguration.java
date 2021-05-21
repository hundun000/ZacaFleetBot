package com.mirai.hundun.cp;

import org.springframework.context.annotation.Bean;

import feign.Logger;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public class PublicFeignConfiguration {
    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
}
