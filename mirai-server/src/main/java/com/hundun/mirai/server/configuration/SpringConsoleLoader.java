package com.hundun.mirai.server.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogic;
import com.hundun.mirai.bot.export.CustomBeanFactory;
import com.hundun.mirai.bot.export.IConsole;
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
    @Autowired
    PrivateSettingsLoader privateSettingsLoader;
    
    @Value("${character.amiya.listenWeiboUids:}")
    public String[] amiyaListenWeiboUids;
    
    @Value("${character.prinzEugen.listenWeiboUids:}")
    public String[] prinzEugenListenWeiboUids;
    
    @Value("${character.zacaMusume.listenWeiboUids:}")
    public String[] zacaMusumeListenWeiboUids;
    
    
    public SpringConsole springConsole;
    
    @PostConstruct
    public void load() {
        log.info("PrivateSettings = {}", privateSettingsLoader.getPrivateSettings());
        
        PublicSettings publicSettings = new PublicSettings();
        publicSettings.amiyaListenWeiboUids = amiyaListenWeiboUids;
        publicSettings.prinzEugenListenWeiboUids = prinzEugenListenWeiboUids;
        publicSettings.zacaMusumeListenWeiboUids = zacaMusumeListenWeiboUids;
        
        springConsole = new SpringConsole(privateSettingsLoader.getPrivateSettings(), publicSettings);

        
    
    }
    
}
