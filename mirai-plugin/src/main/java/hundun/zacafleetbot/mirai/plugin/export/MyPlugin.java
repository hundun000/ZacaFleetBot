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
    
    
    
    public MyPlugin(JvmPluginDescription description) {
        super(description);
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        try {
            
            console = new ConsoleAdapter(this);
            console.laterInitBotLogic(createBotLogic(console));
            
        } catch (Exception e) {
            getLogger().error("onLoad error:", e);
        }

    }
    
    protected abstract BaseBotLogic createBotLogic(IConsole console) throws Exception;
    
    @Override
    public void onEnable() {
        if (console != null) {
            GlobalEventChannel.INSTANCE.registerListenerHost(console);
        } else {
            getLogger().warning("cannot enable");
        }
        
        
    }
    
}
