package com.hundun.mirai.plugin;

import com.hundun.mirai.bot.CustomBeanFactory;
import com.hundun.mirai.bot.configuration.PrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;


/**
 * @author hundun
 * Created on 2021/06/02
 */
@Slf4j
public class PluginMain extends JavaPlugin {
    public static final PluginMain INSTANCE = new PluginMain(); // 可以像 Kotlin 一样静态初始化单例

    
    public PluginMain() {
        super(new JvmPluginDescriptionBuilder(
                "org.example.test-plugin", // name
                "1.0.0" // version
            )
            // .author("...")
            // .info("...")
            .build());
    }
    
    
    
    
    
    @Override
    public void onEnable() {
        
        
        PrivateSettings privateSettings = new PrivateSettings();
        log.info("PrivateSettings = {}", privateSettings);
        
        PublicSettings publicSettings = new PublicSettings();
        
        
        CustomBeanFactory.init(privateSettings, publicSettings);
        BotService botService = CustomBeanFactory.getInstance().botService;
        //botService.login();
    }


}
