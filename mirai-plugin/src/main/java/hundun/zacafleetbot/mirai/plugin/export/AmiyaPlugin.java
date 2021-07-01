package hundun.zacafleetbot.mirai.plugin.export;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.export.BotLogicOfAmiyaAsEventHandler;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author hundun
 * Created on 2021/06/16
 */
public class AmiyaPlugin extends MyPlugin {
    public static final AmiyaPlugin INSTANCE = new AmiyaPlugin();
    
    public AmiyaPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "ZacaFleetBot.Amiya", // name
                "0.1.0" // version
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
