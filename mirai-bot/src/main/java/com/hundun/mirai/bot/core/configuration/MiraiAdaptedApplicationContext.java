package com.hundun.mirai.bot.core.configuration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author hundun
 * Created on 2021/07/01
 */
public class MiraiAdaptedApplicationContext extends AnnotationConfigApplicationContext {
    
    public MiraiAdaptedApplicationContext(boolean lateRefresh) {
        super();
        this.setClassLoader(this.getClass().getClassLoader());
        this.scan("com.hundun.mirai.bot");
        if (!lateRefresh) {
            this.refresh();
        }
    }
}
