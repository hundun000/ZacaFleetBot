package com.mirai.hundun.quiz.feign;



import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.Request;

public class StillstandingApiFeignConfiguration {

    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
    
}
