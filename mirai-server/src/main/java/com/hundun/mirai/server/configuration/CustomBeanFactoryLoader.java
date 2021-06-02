package com.hundun.mirai.server.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.hundun.mirai.bot.CustomBeanFactory;
import com.hundun.mirai.bot.configuration.PublicSettings;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/28
 */
@Slf4j
@Configuration
public class CustomBeanFactoryLoader {
    @Autowired
    PrivateSettingsLoader privateSettingsLoader;
    
    @Value("${character.amiya.listenWeiboUids:}")
    public String[] amiyaListenWeiboUids;
    
    @Value("${character.prinzEugen.listenWeiboUids:}")
    public String[] prinzEugenListenWeiboUids;
    
    @Value("${character.zacaMusume.listenWeiboUids:}")
    public String[] zacaMusumeListenWeiboUids;
    
    @PostConstruct
    public void load() {
        log.info("PrivateSettings = {}", privateSettingsLoader.getPrivateSettings());
        
        PublicSettings publicSettings = new PublicSettings();
        publicSettings.amiyaListenWeiboUids = amiyaListenWeiboUids;
        publicSettings.prinzEugenListenWeiboUids = prinzEugenListenWeiboUids;
        publicSettings.zacaMusumeListenWeiboUids = zacaMusumeListenWeiboUids;
        CustomBeanFactory.init(privateSettingsLoader.getPrivateSettings(), publicSettings);
    }
    
}
