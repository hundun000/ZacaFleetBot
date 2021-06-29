package com.hundun.mirai.bot.core;

import java.io.File;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.character.Amiya;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.EventInfoFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.export.IMyEventHandler;
import com.hundun.mirai.bot.helper.Utils;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Slf4j
public abstract class BaseBotLogic {

    protected IMyEventHandler myEventHandler;
    public AppPrivateSettings appPrivateSettings;
    
    
    public BaseBotLogic(IConsole console, Class<? extends IMyEventHandler> myEventHandlerClass) throws Exception {
        
        File settingsFile = console.resolveConfigFile("private-settings.json");
        AppPrivateSettings appPrivateSettings = Utils.parseAppPrivateSettings(settingsFile);
        
        File publicSettingsFile = console.resolveConfigFile("public-settings.json");
        AppPublicSettings appPublicSettings = Utils.parseAppPublicSettings(publicSettingsFile);
        
        
        SpringContextLoaderThread thread = new SpringContextLoaderThread(this.getClass());
        thread.start();
        thread.join();
        AnnotationConfigApplicationContext context = thread.context;
        
        context.registerBean(AppPrivateSettings.class, () -> appPrivateSettings);
        context.registerBean(AppPublicSettings.class, () -> appPublicSettings);
        context.registerBean(IConsole.class, () -> console);
        context.refresh();
        
        console.getLogger().info("ApplicationContext created, has beans = " + Arrays.toString(context.getBeanDefinitionNames()));
        
        
        
        
        
        console.getLogger().info("ApplicationContext has console = " + (context.getBean(IConsole.class) != null));
        console.getLogger().info("appPrivateSettings = " + context.getBean(AppPrivateSettings.class));
        console.getLogger().info("publicSettings = " + context.getBean(AppPrivateSettings.class));
        
        this.myEventHandler = context.getBean(myEventHandlerClass);
        this.appPrivateSettings = appPrivateSettings;
        

    }

    

    

    public ListeningStatus onMessage(NudgeEvent event) throws Exception {
        EventInfo eventInfo = EventInfoFactory.get(event);
        boolean done = myEventHandler.onNudgeEvent(eventInfo);
        return ListeningStatus.LISTENING;
    }

    public ListeningStatus onMessage(GroupMessageEvent event) throws Exception {
        EventInfo eventInfo = EventInfoFactory.get(event);
        boolean done = myEventHandler.onGroupMessageEvent(eventInfo);
        return ListeningStatus.LISTENING;
    }
    

    
}
