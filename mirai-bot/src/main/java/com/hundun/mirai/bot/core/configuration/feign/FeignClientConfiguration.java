package com.hundun.mirai.bot.core.configuration.feign;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hundun.mirai.bot.cp.kcwiki.feign.KcwikiApiFeignClient;
import com.hundun.mirai.bot.cp.penguin.feign.PenguinApiFeignClient;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboApiFeignClient;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboPictureApiFeignClient;
import com.hundun.mirai.bot.export.IConsole;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;

import feign.Feign;
import feign.Logger;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * @author hundun
 * Created on 2021/07/01
 */
@Configuration
public class FeignClientConfiguration {
    
    Level feignLogLevel = Level.BASIC;
    Encoder feignEncoder = new JacksonEncoder();
    Decoder feignDecoder= new JacksonDecoder();
    
    @Autowired
    IConsole console;
    
    private <T> T get(Class<T> clazz, String url) {
        return Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(new MiraiFeignLogger(clazz.getSimpleName(), "debug", console.getLogger()))
                .logLevel(feignLogLevel)
                .target(clazz, url)
                ;
    }
    
    @Bean
    public KcwikiApiFeignClient kcwikiApiFeignClient() {
        return get(KcwikiApiFeignClient.class, "http://api.kcwiki.moe")
                ;
    }
    
    @Bean
    public WeiboApiFeignClient weiboApiFeignClient() {
        return get(WeiboApiFeignClient.class, "https://m.weibo.cn")
                ;
    }
    
    @Bean
    public WeiboPictureApiFeignClient weiboPictureApiFeignClient() {
        return get(WeiboPictureApiFeignClient.class, "https://wx2.sinaimg.cn")
                ;
    }
    
    @Bean
    public PenguinApiFeignClient penguinApiFeignClient() {
        return get(PenguinApiFeignClient.class, "https://penguin-stats.io/PenguinStats/api")
                ;
    }
    
    @Bean
    public StillstandingApiFeignClient stillstandingApiFeignClient() {
        return get(StillstandingApiFeignClient.class, "http://localhost:10100/api/game")
                ;
    }

}
