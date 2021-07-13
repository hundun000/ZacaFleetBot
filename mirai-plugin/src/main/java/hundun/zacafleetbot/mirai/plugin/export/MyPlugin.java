package hundun.zacafleetbot.mirai.plugin.export;

import org.jetbrains.annotations.NotNull;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import hundun.zacafleetbot.mirai.plugin.ConsoleAdapter;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author hundun
 * Created on 2021/06/21
 */
public abstract class MyPlugin extends JavaPlugin {

    private ConsoleAdapter console;
    
    boolean needRegisterListenerHost = true;
    
    public MyPlugin(JvmPluginDescription description) {
        super(description);
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) { 
        console = new ConsoleAdapter(this);
        getLogger().info("onLoad success");
    }
    
    protected abstract BaseBotLogic createBotLogic(IConsole console) throws Exception;
    
    @Override
    public void onEnable() {
        getLogger().info("onEnable called");
        try {
            BaseBotLogic botLogicImpl = createBotLogic(console);
            console.enablePluginAndSetBotLogic(botLogicImpl);
            if (needRegisterListenerHost) {
                GlobalEventChannel.INSTANCE.registerListenerHost(console);
                needRegisterListenerHost = false;
            }
        } catch (Exception e) {
            getLogger().error("onEnable error:", e);
        }
        
        
    }
    
    @Override
    public void onDisable() {
        getLogger().info("onDisable called");
        try {
            console.disablePluginAndClearBotLogic();
        } catch (Exception e) {
            getLogger().error("onDisable error:", e);
        }
    }
    
}
