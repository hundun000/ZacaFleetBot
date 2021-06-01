package com.hundun.mirai.bot.cp.kcwiki.feign;

import com.hundun.mirai.bot.cp.kcwiki.domain.dto.KcwikiInitEquip;
import com.hundun.mirai.bot.cp.kcwiki.domain.dto.KcwikiShipDetail;

import feign.Param;
import feign.RequestLine;

/**
 * @author hundun
 * Created on 2021/05/21
 */
//@FeignClient(
//        name = "kcwikiApiFeignClient",
//        url = "http://api.kcwiki.moe"
//)
public interface KcwikiApiFeignClient {
    
    @RequestLine("GET /ship/detail/{id}")
    KcwikiShipDetail shipDetail(@Param("id") int id);
    
    @RequestLine("GET /ship/detail/{name}")
    KcwikiShipDetail shipDetail(@Param("name") String name);
    
    @RequestLine("GET /init/equip/{id}")
    KcwikiInitEquip initEquip(@Param("id") int id);
}
