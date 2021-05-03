package com.mirai.hundun.cp.weibo.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import feign.Response;






@FeignClient(
        name = "weiboApiService",
        url = "https://m.weibo.cn",
        configuration = WeiboApiFeignConfiguration.class
)
@Component
public interface WeiboApiService {
    @RequestMapping(
            value = "/api/container/getIndex", 
            method = RequestMethod.GET,
            headers = {
                    "Content-Type=text/plain; charset=utf-8", 
                    "Accept-Language=zh-CN,zh;q=0.9",
                    "User-Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
            }
            )
    String get(
            @RequestParam("uid") String uid,
            @RequestParam("type") String type,
            @RequestParam("value") String value,
            @RequestParam("containerid") String containerid 
            );
    
    
    
    @RequestMapping(
            value = "/statuses/extend", 
            method = RequestMethod.GET,
            headers = {
                    "Content-Type=text/plain; charset=utf-8", 
                    "Accept-Language=zh-CN,zh;q=0.9",
                    "User-Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
            }
    )
    String blogDetail(@RequestParam("id") String id);
    
    
    
}
