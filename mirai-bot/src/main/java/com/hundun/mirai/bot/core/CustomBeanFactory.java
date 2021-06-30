package com.hundun.mirai.bot.core;


import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.hundun.mirai.bot.core.function.reminder.RemiderTaskRepository;
import com.hundun.mirai.bot.core.function.reminder.ReminderTask;
import com.hundun.mirai.bot.cp.FeignLogger;
import com.hundun.mirai.bot.cp.kcwiki.feign.KcwikiApiFeignClient;
import com.hundun.mirai.bot.cp.penguin.feign.PenguinApiFeignClient;
import com.hundun.mirai.bot.cp.weibo.db.WeiboCardCacheRepository;
import com.hundun.mirai.bot.cp.weibo.db.WeiboUserInfoCacheRepository;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboApiFeignClient;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboPictureApiFeignClient;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;

import feign.Feign;
import feign.Logger;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 一步步把SpringBean替换掉
 * @author hundun
 * Created on 2021/05/25
 */
@Slf4j
public class CustomBeanFactory {

    
    private static CustomBeanFactory instance = new CustomBeanFactory();

    public static CustomBeanFactory getInstance() {
        return instance;
    }


    
    private CustomBeanFactory() {
        

        Level feignLogLevel = Level.NONE;
        Logger feignLogger = new FeignLogger("feign", "debug");
        Encoder feignEncoder = new JacksonEncoder();
        Decoder feignDecoder = new JacksonDecoder();
        this.kcwikiApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(KcwikiApiFeignClient.class, "http://api.kcwiki.moe")
                ;
        this.weiboApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(WeiboApiFeignClient.class, "https://m.weibo.cn")
                ;
        this.weiboPictureApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(WeiboPictureApiFeignClient.class, "https://wx2.sinaimg.cn")
                ;
        this.penguinApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(PenguinApiFeignClient.class, "https://penguin-stats.io/PenguinStats/api")
                ;
        
        this.stillstandingApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(StillstandingApiFeignClient.class, "http://localhost:10100/api/game")
                ;
    }
    
    //public AppPrivateSettings appPrivateSettings;
    //public AppPublicSettings appPublicSettings;
    //public IConsole console;
    

    
    // ----- weibo -----
    public WeiboApiFeignClient weiboApiFeignClient;
    public WeiboPictureApiFeignClient weiboPictureApiFeignClient;



    // ----- Stillstanding -----
    public StillstandingApiFeignClient stillstandingApiFeignClient;

    // ----- Penguin -----
    public PenguinApiFeignClient penguinApiFeignClient;
    

    // ----- Kcwiki -----
    public KcwikiApiFeignClient kcwikiApiFeignClient;

    





    
}
