package com.hundun.mirai.bot.core;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author hundun
 * Created on 2021/06/24
 */
public interface IPostConsoleBind {
    void postConsoleBind();
}
