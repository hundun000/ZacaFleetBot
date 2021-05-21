package com.mirai.hundun.cp.penguin.feign;



import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.mirai.hundun.cp.PublicFeignConfiguration;
import com.mirai.hundun.cp.penguin.domain.Item;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixResponse;
import com.mirai.hundun.cp.penguin.domain.Stage;






@FeignClient(
        name = "penguinApiService",
        url = "https://penguin-stats.io/PenguinStats/api",
        configuration = PublicFeignConfiguration.class
)
@Component
public interface PenguinApiService {
    @RequestMapping(
            value = "/v2/items", 
            method = RequestMethod.GET,
            headers = {
                    "Content-Type=application/json; charset=utf-8"
            }
            )
    List<Item> items();
    
    @RequestMapping(
            value = "/v2/stages?server=CN", 
            method = RequestMethod.GET,
            headers = {
                    "Content-Type=application/json; charset=utf-8"
            }
            )
    List<Stage> stages();
    
    @RequestMapping(
            value = "/v2/result/matrix?is_personal=false&server=CN&show_closed_zones=false", 
            method = RequestMethod.GET,
            headers = {
                    "Content-Type=application/json; charset=utf-8"
            }
            )
    ResultMatrixResponse resultMatrix(@RequestParam("itemFilter") String itemFilter);

}
