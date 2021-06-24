package com.hundun.mirai.server.configuration;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.Utils;
import com.hundun.mirai.server.SpringConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author hundun
 * Created on 2021/05/28
 */
@Slf4j
@Configuration
public class SpringConsoleLoader {
//    @Autowired
//    PrivateSettingsLoader privateSettingsLoader;
    

    
    public SpringConsole console;
    
    @PostConstruct
    public void loadAndEnable() {
        

        onLoad();
        onEnable();
    
    }
    
    private void onLoad() {
        
        try {

            console = new SpringConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    private void onEnable() {
        if (console != null) {
            GlobalEventChannel.INSTANCE.registerListenerHost(console);
        } else {
            log.error("cannot enable");
        }
    }
    
}
