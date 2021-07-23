

import hundun.zacafleetbot.mirai.plugin.export.AmiyaPlugin;
import hundun.zacafleetbot.mirai.plugin.export.RouterPlugin;
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
public class RouterPluginTest {
    public static void main(String[] args) throws InterruptedException {
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
        
        PluginManager.INSTANCE.loadPlugin(RouterPlugin.INSTANCE);
        
        PluginManager.INSTANCE.enablePlugin(RouterPlugin.INSTANCE);
//        Thread.sleep(3000);
//        PluginManager.INSTANCE.disablePlugin(RouterPlugin.INSTANCE);
//        
//        // enable again
//        Thread.sleep(3000);
//        PluginManager.INSTANCE.enablePlugin(RouterPlugin.INSTANCE);
//        Thread.sleep(3000);
//        PluginManager.INSTANCE.disablePlugin(RouterPlugin.INSTANCE);
        
    }
}
