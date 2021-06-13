package com.hundun.mirai.plugin;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogic;
import com.hundun.mirai.bot.export.CharacterRouter;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class ConsoleAdapter implements IConsole, ListenerHost {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";
    
    boolean isBotOnline = false;
    
    AppPrivateSettings appPrivateSettings;
    
    BotLogic botLogic;
    
    public ConsoleAdapter(AppPrivateSettings appPrivateSettings, PublicSettings publicSettings) {
        this.appPrivateSettings = appPrivateSettings;
        
        botLogic = new BotLogic(appPrivateSettings, publicSettings, this);
    }
    

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception { 
        return botLogic.onMessage(event);
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        return botLogic.onMessage(event);
    }
    


    @Override
    public void sendToGroup(Bot bot, long groupId, MessageChain messageChain) {
        if (isBotOnline) {
            // TODO
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }

    @Override
    public Image uploadImage(Bot bot, long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
            // TODO
            return null;
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
         // TODO
            return null;
        } else {
            log.info("[offline mode]uploadVoice groupId = {}", groupId);
            return new Voice("", new byte[1], 0, 0, "");
        }
    }
    
    @Override
    public List<Bot> getBots() {
     // TODO
        return null;
    }





    @Override
    public Bot getBot(long botId) {
        // TODO Auto-generated method stub
        return null;
    }

}
