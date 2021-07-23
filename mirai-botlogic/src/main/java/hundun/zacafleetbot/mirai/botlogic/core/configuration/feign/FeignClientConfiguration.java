package hundun.zacafleetbot.mirai.botlogic.core.configuration.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.feign.KcwikiApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.feign.PenguinApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.pixiv.feign.RainchanPixivFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.feign.WeiboApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.feign.WeiboPictureApiFeignClient;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;

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
    public RainchanPixivFeignClient rainchanPixivFeignClient() {
        return get(RainchanPixivFeignClient.class, "https://pximg.rainchan.win")
                ;
    }

}
