package com.hundun.mirai.plugin.cp.kcwiki.feign;

import java.util.List;


import com.hundun.mirai.plugin.cp.kcwiki.domain.dto.KcwikiInitEquip;
import com.hundun.mirai.plugin.cp.kcwiki.domain.dto.KcwikiShipDetail;
import com.hundun.mirai.plugin.cp.penguin.domain.Item;

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
    
    @RequestLine("GET /ship/detail/{id}")
    KcwikiShipDetail shipDetail(@Param("name") String name);
    
    @RequestLine("GET /init/equip/{id}")
    KcwikiInitEquip initEquip(@Param("id") int id);
}
