package com.hundun.mirai.plugin.export;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfAmiyaAsEventHandler;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.Utils;
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
public class AmiyaPlugin extends MyPlugin {
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

    @Override
    protected BaseBotLogic getBotLogicImpl(
            IConsole console) throws Exception {
        return new BotLogicOfAmiyaAsEventHandler(console);
    }
    
    
    
    
}
