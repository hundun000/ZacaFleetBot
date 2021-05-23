package com.mirai.hundun.cp.kcwiki.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mirai.hundun.cp.PublicFeignConfiguration;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiInitEquip;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipDetail;
import com.mirai.hundun.cp.penguin.domain.Item;

/**
 * @author hundun
 * Created on 2021/05/21
 */
@FeignClient(
        name = "kcwikiApiFeignClient",
        url = "http://api.kcwiki.moe",
        configuration = PublicFeignConfiguration.class
)
@Component
public interface KcwikiApiFeignClient {
    @RequestMapping(
            value = "/ship/detail/{id}", 
            method = RequestMethod.GET
            )
    KcwikiShipDetail shipDetail(@PathVariable("id") int id);
    
    
    @RequestMapping(
            value = "/ship/detail/{name}", 
            method = RequestMethod.GET
            )
    KcwikiShipDetail shipDetail(@PathVariable("name") String name);
    
    @RequestMapping(
            value = "/init/equip/{id}", 
            method = RequestMethod.GET
            )
    KcwikiInitEquip initEquip(@PathVariable("id") int id);
}
