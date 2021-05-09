package com.zaca.stillstanding.domain.dto;
/**
 * @author hundun
 * Created on 2019/11/04
 */
public interface IApiResult {
    
    int getRetcode();

    String getMessage();

    int getStatus();

    String getPayload();

}
