package com.hundun.mirai.bot.cp.weibo.feign;


import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;






//@FeignClient(
//        name = "weiboPictureApiService",
//        url = "https://wx2.sinaimg.cn",
//        configuration = PublicFeignConfiguration.class
//)
public interface WeiboPictureApiFeignClient {
    
    @RequestLine("GET /large/{id}")
    @Headers({
        "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
        })
    Response pictures(@Param("id") String id);
}
