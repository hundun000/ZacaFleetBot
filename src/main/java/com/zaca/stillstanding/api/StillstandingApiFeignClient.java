package com.zaca.stillstanding.api;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.zaca.stillstanding.domain.dto.ApiResult;

import feign.Response;






@FeignClient(
        name = "stillstandingApiService",
        url = "http://localhost:10100/api/game",
        configuration = StillstandingApiFeignConfiguration.class
)
@Component
public interface StillstandingApiFeignClient extends StillstandingApi {
    
    
    @RequestMapping(
            value = "/pictures", 
            method = RequestMethod.GET
    )
    Response pictures(@RequestParam("id") String imageResourceId);
    
}
