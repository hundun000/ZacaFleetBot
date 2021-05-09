package com.zaca.stillstanding.domain.dto;


import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * @author hundun
 * Created on 2019/11/04
 */
public class ApiResult implements IApiResult {
    
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final int SUCCESS_STATUS = 200;
    private static final int FAIL_STATUS = 400;
    
    private final String message;
    private final int status;
    private final String payload;
    private final int retcode;
    
    public ApiResult() {
        this((String) null);
    }
    
    public ApiResult(IApiResult apiResult) {
        this.message = apiResult.getMessage();
        this.status = FAIL_STATUS;
        this.payload = apiResult.getPayload();
        this.retcode = apiResult.getRetcode();
    }
    
    public ApiResult(String payload) {
        this.message = SUCCESS_MESSAGE;
        this.status = SUCCESS_STATUS;
        this.payload = payload;
        this.retcode = 0;
    }


    
    public ApiResult(JsonProcessingException e) {
        this.message = e.getMessage();
        this.status = FAIL_STATUS;
        this.payload = null;
        this.retcode = -1;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    public int getStatus() {
        return status;
    }
    
    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public int getRetcode() {
        return retcode;
    }

    
}
