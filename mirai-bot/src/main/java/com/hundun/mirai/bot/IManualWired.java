package com.hundun.mirai.bot;


/**
 * @author hundun
 * Created on 2021/05/26
 */
public interface IManualWired {
    void manualWired();
    default void afterManualWired() {};
}
