package com.mirai.hundun.cp.penguin.feign;

import org.springframework.context.annotation.Bean;

import feign.Logger;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public class PenguinApiFeignConfiguration {
    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
}
