package com.hundun.mirai.plugin;

import com.hundun.mirai.plugin.amiya.AmiyaPlugin;

import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.GlobalScope;
import net.mamoe.mirai.console.MiraiConsole;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
import net.mamoe.mirai.console.plugin.PluginManager;
/**
 * @author hundun
 * Created on 2021/06/03
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
     
        //MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
        MiraiConsoleTerminalLoader.main(args);
        
        PluginManager.INSTANCE.loadPlugin(AmiyaPlugin.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(AmiyaPlugin.INSTANCE);
        
    }
}
