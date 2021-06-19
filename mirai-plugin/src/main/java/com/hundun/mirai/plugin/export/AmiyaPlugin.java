package com.hundun.mirai.plugin.export;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfAmiyaAsEventHandler;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.plugin.ConsoleAdapter;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author hundun
 * Created on 2021/06/16
 */
@Slf4j
public class AmiyaPlugin extends JavaPlugin {
    public static final AmiyaPlugin INSTANCE = new AmiyaPlugin();
    public AmiyaPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "com.hundun.AmiyaPlugin", // name
                "0.0.1" // version
            )
            // .author("...")
            // .info("...")
            .build());
    }
    
    ConsoleAdapter console;
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        log.info("Amiya onLoad!");
        getLogger().info("Amiya onLoad!");
        
        AppPrivateSettings appPrivateSettings = null;
        PublicSettings publicSettings = null;
        
        console = new ConsoleAdapter(appPrivateSettings, publicSettings);
        
        console.laterInitBotLogic(new BotLogicOfAmiyaAsEventHandler(appPrivateSettings, publicSettings, console));

    }
    
    
    
    @Override
    public void onEnable() {
        log.info("Amiya onEnable!");
        getLogger().info("Amiya onEnable!");
        GlobalEventChannel.INSTANCE.registerListenerHost(console);
    }
}
