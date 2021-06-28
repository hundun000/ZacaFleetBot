package com.hundun.mirai.bot.export;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hundun.mirai.bot.core.SpringContextLoaderThread;
import com.hundun.mirai.bot.core.character.Amiya;

import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/06/29
 */
public class DemoBotLogic implements ListenerHost {
    
    JvmPlugin parent;
    
    AnnotationConfigApplicationContext springContext;
    
    public DemoBotLogic(JvmPlugin plugin) {
        this.parent = plugin;
        
        try {
            SpringContextLoaderThread thread = new SpringContextLoaderThread(this.getClass());
            thread.start();
            thread.join();
            this.springContext = thread.context;
            parent.getLogger().info("ApplicationContext created, has beans = " + Arrays.toString(this.springContext.getBeanDefinitionNames()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        DemoBean bean = springContext.getBean(DemoBean.class);
        event.getGroup().sendMessage(bean.service());
        return ListeningStatus.LISTENING;
    }
    
    
}
