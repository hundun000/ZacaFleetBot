package com.mirai.hundun.cp.quiz.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;






@FeignClient(
        name = "stillstandingApiService",
        url = "http://localhost:10100/api",
        configuration = StillstandingApiFeignConfiguration.class,
        fallbackFactory = StillstandingApiFallbackFactory.class
)
@Component
public interface StillstandingApiService {
    
    @RequestMapping(
            value = "/match/createEndlessMatch?teamNames=方舟同好组", 
            method = RequestMethod.POST
            )
    String createEndlessMatch(
            );
    
    
    
    @RequestMapping(
            value = "/match/start", 
            method = RequestMethod.POST
    )
    String start(@RequestParam("matchId") int id);
    
    @RequestMapping(
            value = "/match/answer", 
            method = RequestMethod.POST
    )
    String answer(@RequestParam("matchId") int id, @RequestParam("answer") String answer);
}
