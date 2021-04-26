package com.mirai.hundun.cp.quiz.feign;



import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.Request;

public class StillstandingApiFeignConfiguration {

    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
    
}
