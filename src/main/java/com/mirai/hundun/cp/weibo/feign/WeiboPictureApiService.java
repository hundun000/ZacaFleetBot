package com.mirai.hundun.cp.weibo.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import feign.Response;






@FeignClient(
        name = "weiboPictureApiService",
        url = "https://wx2.sinaimg.cn",
        configuration = WeiboApiFeignConfiguration.class
)
@Component
public interface WeiboPictureApiService {
    
    
    
    @RequestMapping(
            value = "/large/{id}", 
            method = RequestMethod.GET,
            headers = {
                    "User-Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
            }
    )
    Response pictures(@PathVariable("id") String id);
}
