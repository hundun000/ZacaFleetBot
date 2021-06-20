package com.hundun.mirai.plugin;

import java.io.File;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.CharacterRouter;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.plugin.export.MyPlugin;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class ConsoleAdapter implements IConsole, ListenerHost {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";
    
    
    BaseBotLogic botLogic;
    
    final JvmPlugin plugin;
    
    public ConsoleAdapter(JvmPlugin plugin) {
        
        this.plugin = plugin;

    }
    
    public void laterInitBotLogic(BaseBotLogic botLogic) {
        this.botLogic = botLogic;
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
        if (bot != null) {
            bot.getGroupOrFail(groupId).sendMessage(messageChain);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }

    @Override
    public Image uploadImage(Bot bot, long groupId, ExternalResource externalResource) {
        if (bot != null) {
            return bot.getGroupOrFail(groupId).uploadImage(externalResource);
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource) {
        if (bot != null) {
            return bot.getGroupOrFail(groupId).uploadVoice(externalResource);
        } else {
            log.info("[offline mode]uploadVoice groupId = {}", groupId);
            return new Voice("", new byte[1], 0, 0, "");
        }
    }
    
    @Override
    public List<Bot> getBots() {
        return Bot.getInstances();
    }

    @Override
    public Bot getBotOrNull(long botId) {
        return Bot.getInstanceOrNull(botId);
    }

    @Override
    public File resolveDataFile(String subPathName) {
        return plugin.resolveDataFile(subPathName);
    }

    @Override
    public File resolveConfigFile(String subPathName) {
        return plugin.resolveConfigFile(subPathName);
    }

    @Override
    public MiraiLogger getLogger() {
        return plugin.getLogger();
    }

}
