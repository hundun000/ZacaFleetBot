package com.hundun.mirai.bot.export;

import com.hundun.mirai.bot.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Slf4j
public class BotLogic {
    
    
    private CharacterRouter characterRouter;
    
    public BotLogic(AppPrivateSettings appPrivateSettings, PublicSettings publicSettings, IConsole consoleImplement) {
        CustomBeanFactory.init(appPrivateSettings, publicSettings, consoleImplement);
        this.characterRouter = CustomBeanFactory.getInstance().characterRouter;
    }
    
    public CharacterRouter getBotLogiclistenerHost() {
        return characterRouter;
    }

    public ListeningStatus onMessage(NudgeEvent event) throws Exception {
        return characterRouter.onMessage(event);
    }

    public ListeningStatus onMessage(GroupMessageEvent event) throws Exception {
        return characterRouter.onMessage(event);
    }
    
    public void onEnable() {
        log.info("BotLogic onEnable");
        GlobalEventChannel.INSTANCE.registerListenerHost(characterRouter);
    }
    
}
