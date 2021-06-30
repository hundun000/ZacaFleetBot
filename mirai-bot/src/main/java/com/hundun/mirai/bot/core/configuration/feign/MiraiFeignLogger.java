package com.hundun.mirai.bot.core.configuration.feign;

import java.io.IOException;

import com.hundun.mirai.bot.export.IConsole;

import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2021/03/31
 */
public class MiraiFeignLogger extends Logger {
    MiraiLogger miraiLogger;
    String tagName;
    String level;
    public MiraiFeignLogger(String tagName, String level, MiraiLogger miraiLogger) {
        this.tagName = tagName;
        this.level = level;
        this.miraiLogger = miraiLogger;
    }
    
    @Override
    protected void log(String configKey, String format, Object... args) {
        String message = "[" + tagName + "] " + String.format(methodTag(configKey) + format, args);
        switch (level) {
            case "info":
                miraiLogger.info(message);
                break;
            case "debug":
            default:
                miraiLogger.debug(message);
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
