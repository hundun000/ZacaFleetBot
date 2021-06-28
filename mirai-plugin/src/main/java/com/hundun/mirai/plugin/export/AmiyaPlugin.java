package com.hundun.mirai.plugin.export;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.export.BotLogicOfAmiyaAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.plugin.ConsoleAdapter;

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

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
    protected BaseBotLogic createBotLogic(IConsole console) throws Exception {
        return new BotLogicOfAmiyaAsEventHandler(console);
    }


    
    
    
}
