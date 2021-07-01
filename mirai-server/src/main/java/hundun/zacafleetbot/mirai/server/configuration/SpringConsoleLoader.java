package hundun.zacafleetbot.mirai.server.configuration;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPrivateSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.export.BotLogicOfCharacterRouterAsEventHandler;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import hundun.zacafleetbot.mirai.botlogic.helper.Utils;
import hundun.zacafleetbot.mirai.server.SpringConsole;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.GlobalEventChannel;

/**
 * @author hundun
 * Created on 2021/05/28
 */
@Slf4j
@Configuration
public class SpringConsoleLoader {
//    @Autowired
//    PrivateSettingsLoader privateSettingsLoader;
    

    public SpringConsole console;
    
    @PostConstruct
    public void loadAndEnable() {
        

        onLoad();
        onEnable();
    
    }
    
    private void onLoad() {
        
        
        try {
            console = new SpringConsole();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    private void onEnable() {
        if (console != null) {
            GlobalEventChannel.INSTANCE.registerListenerHost(console);
        } else {
            log.error("cannot enable");
        }
    }
    
}
