package com.hundun.mirai.plugin.export;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfAmiyaAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.Utils;
import com.hundun.mirai.plugin.ConsoleAdapter;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author hundun
 * Created on 2021/06/21
 */
public abstract class MyPlugin extends JavaPlugin {

    ConsoleAdapter console;
    
    public MyPlugin(JvmPluginDescription description) {
        super(description);
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        
        try {

            console = new ConsoleAdapter(this);
            
            console.laterInitBotLogic(getBotLogicImpl(console));

        } catch (Exception e) {
            getLogger().error("onLoad error:" + e.getMessage());
        }

    }
    
    
    protected abstract BaseBotLogic getBotLogicImpl(IConsole console) throws Exception;
    
    
    @Override
    public void onEnable() {
        if (console != null) {
            GlobalEventChannel.INSTANCE.registerListenerHost(console);
        } else {
            getLogger().warning("cannot enable");
        }
        
        
    }
    
}
