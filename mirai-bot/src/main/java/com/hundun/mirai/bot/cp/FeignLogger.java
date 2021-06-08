package com.hundun.mirai.bot.cp;

import java.io.IOException;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/03/31
 */
@Slf4j
public class FeignLogger extends Logger {

    String tagName;
    String level;
    public FeignLogger(String tagName, String level) {
        this.tagName = tagName;
        this.level = level;
    }
    
    @Override
    protected void log(String configKey, String format, Object... args) {
        String message = "[" + tagName + "] " + String.format(methodTag(configKey) + format, args);
        switch (level) {
            case "info":
                log.info(message);
                break;
            case "debug":
            default:
                log.debug(message);
                break;
            }
        
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        log(configKey, "%s %s HTTP/1.1, log request begin. <---", request.httpMethod().name(), request.url());
        super.logRequest(configKey, logLevel, request);
        log(configKey, "%s %s HTTP/1.1, log request end. <---", request.httpMethod().name(), request.url());
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        String reason = response.reason() != null && logLevel.compareTo(Logger.Level.NONE) > 0 ? " " + response.reason() : "";
        int status = response.status();
        log(configKey, "%s%s HTTP/1.1 (%sms) log logAndRebufferResponse begin. <---", status, reason, elapsedTime);
        Response response1 = super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
        log(configKey, "%s%s HTTP/1.1 (%sms) log logAndRebufferResponse end. <---", status, reason, elapsedTime);
        return response1;
    }
}
