package com.hundun.mirai.plugin.cp.weibo.feign;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface WeiboApiService {
    
    @RequestLine("GET /api/container/getIndex")
    @Headers({
        "Content-Type=text/plain; charset=utf-8", 
        "Accept-Language=zh-CN,zh;q=0.9",
        "User-Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
        })
    String get(
            @Param("uid") String uid,
            @Param("type") String type,
            @Param("value") String value,
            @Param("containerid") String containerid 
            );
    
    
    @RequestLine("GET /statuses/extend")
    @Headers({
        "Content-Type=text/plain; charset=utf-8", 
        "Accept-Language=zh-CN,zh;q=0.9",
        "User-Agent=Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1"
        })
    String blogDetail(@Param("id") String id);
    
    
    
}
