package hundun.zacafleetbot.mirai.plugin.export;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.export.BotLogicOfCharacterRouterAsEventHandler;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;


/**
 * @author hundun
 * Created on 2021/06/02
 */
public class RouterPlugin extends MyPlugin {
    public static final RouterPlugin INSTANCE = new RouterPlugin(); // 可以像 Kotlin 一样静态初始化单例
    
    
    public RouterPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "hundun.zacafleetbot.router", // name
                "0.1.0" // version
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
