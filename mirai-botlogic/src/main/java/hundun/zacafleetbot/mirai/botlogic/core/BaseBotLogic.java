package hundun.zacafleetbot.mirai.botlogic.core;

import java.io.File;
import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hundun.zacafleetbot.mirai.botlogic.core.configuration.MiraiAdaptedApplicationContext;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfoFactory;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPrivateSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import hundun.zacafleetbot.mirai.botlogic.export.IMyEventHandler;
import hundun.zacafleetbot.mirai.botlogic.helper.Utils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Slf4j
public abstract class BaseBotLogic {

    protected IMyEventHandler myEventHandler;
    public AppPrivateSettings appPrivateSettings;
    
    
    public BaseBotLogic(IConsole console, Class<? extends IMyEventHandler> myEventHandlerClass) throws Exception {
        
        File settingsFile = console.resolveConfigFile("private-settings.json");
        AppPrivateSettings appPrivateSettings = Utils.parseAppPrivateSettings(settingsFile);
        
        File publicSettingsFile = console.resolveConfigFile("public-settings.json");
        AppPublicSettings appPublicSettings = Utils.parseAppPublicSettings(publicSettingsFile);
        
        
        @SuppressWarnings("resource")
        AnnotationConfigApplicationContext context = new MiraiAdaptedApplicationContext(true);
        
        context.registerBean(AppPrivateSettings.class, () -> appPrivateSettings);
        context.registerBean(AppPublicSettings.class, () -> appPublicSettings);
        context.registerBean(IConsole.class, () -> console);
        context.refresh();
        
        console.getLogger().info("ApplicationContext created, has beans = " + Arrays.toString(context.getBeanDefinitionNames()));
        
        console.getLogger().info("ApplicationContext has console = " + (context.getBean(IConsole.class) != null));
        console.getLogger().info("appPrivateSettings = " + context.getBean(AppPrivateSettings.class));
        console.getLogger().info("publicSettings = " + context.getBean(AppPrivateSettings.class));
        
        this.myEventHandler = context.getBean(myEventHandlerClass);
        this.appPrivateSettings = appPrivateSettings;
        

    }

    

    

    public ListeningStatus onMessage(NudgeEvent event) throws Exception {
        EventInfo eventInfo = EventInfoFactory.get(event);
        boolean done = myEventHandler.onNudgeEvent(eventInfo);
        return ListeningStatus.LISTENING;
    }

    public ListeningStatus onMessage(GroupMessageEvent event) throws Exception {
        EventInfo eventInfo = EventInfoFactory.get(event);
        boolean done = myEventHandler.onGroupMessageEvent(eventInfo);
        return ListeningStatus.LISTENING;
    }
    

    
}