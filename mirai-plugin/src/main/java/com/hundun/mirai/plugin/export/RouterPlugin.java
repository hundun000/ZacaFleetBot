package com.hundun.mirai.plugin.export;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.plugin.ConsoleAdapter;

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;


/**
 * @author hundun
 * Created on 2021/06/02
 */
public class RouterPlugin extends MyPlugin {
    public static final RouterPlugin INSTANCE = new RouterPlugin(); // 可以像 Kotlin 一样静态初始化单例
    
    
    public RouterPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "org.example.RouterPlugin", // name
                "1.0.0" // version
            )
            // .author("...")
            // .info("...")
            .build());
    }


    @Override
    protected BaseBotLogic createBotLogic(IConsole console) throws Exception {
        return new BotLogicOfCharacterRouterAsEventHandler(console);
    }





}
