package com.hundun.mirai.bot.core;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author hundun
 * Created on 2021/06/29
 */
public class SpringContextLoaderThread extends Thread {
    Class<?> caller;
    public AnnotationConfigApplicationContext context;
    
    public SpringContextLoaderThread(Class<?> caller) {
        this.caller = caller;
    }
    
    @Override
    public void run() {
        this.setContextClassLoader(caller.getClassLoader());
        context = new AnnotationConfigApplicationContext();
        context.scan("com.hundun.mirai.bot");
        context.refresh();
    }
}
