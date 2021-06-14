package com.hundun.mirai.bot.core;

import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.EventInfoFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.export.IMyEventHandler;

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
public abstract class BaseBotLogic {
    
    
    protected IMyEventHandler myEventHandler;
    
    public BaseBotLogic(AppPrivateSettings appPrivateSettings, PublicSettings publicSettings, IConsole consoleImplement) {
        CustomBeanFactory.init(appPrivateSettings, publicSettings, consoleImplement);
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
