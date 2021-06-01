package com.zaca.stillstanding.api;





import feign.Param;
import feign.RequestLine;
import feign.Response;

//@FeignClient(
//        name = "stillstandingApiService",
//        url = "http://localhost:10100/api/game",
//        configuration = StillstandingApiFeignConfiguration.class
//)
public interface StillstandingApiFeignClient extends StillstandingApi {

    @RequestLine("GET /large/{id}")
    Response pictures(@Param("id") String id);
    
}
