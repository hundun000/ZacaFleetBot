package com.mirai.hundun.cp.weibo.feign;



import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.Request;

public class WeiboApiFeignConfiguration {

    @Bean
    Logger.Level level() {
        return Logger.Level.BASIC;
    }
    
}
