package com.zaca.stillstanding.api;




import feign.Response;


public interface StillstandingApiFeignClient extends StillstandingApi {

    Response pictures(String imageResourceId);
    
}
