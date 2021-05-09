package com.zaca.stillstanding.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zaca.stillstanding.domain.dto.ApiResult;
import com.zaca.stillstanding.domain.dto.MatchConfigDTO;

import feign.Response;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface StillstandingApi {

    @RequestMapping(
            value = "/createEndlessMatch", 
            method = RequestMethod.POST
            )
    ApiResult createEndlessMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            );
    
    
    
    @RequestMapping(
            value = "/start", 
            method = RequestMethod.POST
    )
    ApiResult start(@RequestParam(value = "sessionId") String sessionId);
    
    @RequestMapping(
            value = "/nextQustion", 
            method = RequestMethod.POST
    )
    ApiResult nextQustion(
            @RequestParam(value = "sessionId") String sessionId
            );
    
    @RequestMapping(
            value = "/answer", 
            method = RequestMethod.POST
    )
    ApiResult teamAnswer(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "answer") String answer
            );
    

}
