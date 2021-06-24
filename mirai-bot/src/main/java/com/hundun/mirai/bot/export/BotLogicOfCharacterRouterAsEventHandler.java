package com.hundun.mirai.bot.export;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.EventInfoFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;

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
public class BotLogicOfCharacterRouterAsEventHandler extends BaseBotLogic {
    
    
    public BotLogicOfCharacterRouterAsEventHandler(IConsole console) throws Exception {
        super(console);
        this.myEventHandler = CustomBeanFactory.getInstance().characterRouter;
    }
    
    

    
}
