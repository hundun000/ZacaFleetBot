package com.mirai.hundun.cp.weibo.feign;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;




/**
 *
 * @author yiklam
 * @date 2021/1/28
 */
@Slf4j
@Component
public class WeiboApiFallbackFactory implements FallbackFactory<WeiboApiService> {
    @Override
    public WeiboApiService create(Throwable cause) {
        return new WeiboApiService() {

            @Override
            public String get(String uid, String type, String value, String containerid) {
                log.warn("WeiboApiService get fail: ", cause);
                return null;
            }

            @Override
            public String blogDetail(String id) {
                log.warn("WeiboApiService blogDetail fail: ", cause);
                return null;
            }



        };
    }
}
