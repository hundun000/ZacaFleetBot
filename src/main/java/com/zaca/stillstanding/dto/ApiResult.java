package com.zaca.stillstanding.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;

import lombok.Data;


/**
 * @author hundun
 * Created on 2019/11/04
 * @param <T>
 */
@Data
public class ApiResult<T> {
    
    private static final String SUCCESS_MESSAGE = "SUCCESS";
    private static final int SUCCESS_STATUS = 200;
    private static final int FAIL_STATUS = 400;
    
    String message;
    int status;
    T payload;
    int retcode;
    
    public ApiResult() {
        
    }

    public ApiResult(String failMessage, int retcode) {
        this.message = failMessage;
        this.status = FAIL_STATUS;
        this.payload = null;
        this.retcode = -1;
    }

    
    public ApiResult(T payload) {
        this.message = SUCCESS_MESSAGE;
        this.status = SUCCESS_STATUS;
        this.payload = payload;
        this.retcode = 0;
    }


    
}
