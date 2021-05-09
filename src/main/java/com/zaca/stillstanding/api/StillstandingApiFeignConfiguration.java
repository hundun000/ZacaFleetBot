package com.zaca.stillstanding.api;



import org.springframework.context.annotation.Bean;

import feign.Logger;

public class StillstandingApiFeignConfiguration {

    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
    
}
