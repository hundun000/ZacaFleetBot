package com.hundun.mirai.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.data.BotPrivateSettings;
import com.hundun.mirai.bot.export.BotLogic;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class SpringConsole implements IConsole, ListenerHost {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";

    
    //private Bot miraiBot;
    
    private Map<Long, Bot> bots = new HashMap<>();
    
    BotLogic botLogic;
    
    
    AppPrivateSettings appPrivateSettings;
    
  //设备认证信息文件
    //private final String deviceInfoPath = "device.json";
    
    
    public SpringConsole(AppPrivateSettings appPrivateSettings, PublicSettings publicSettings) {
        this.appPrivateSettings = appPrivateSettings;
        
        this.botLogic = new BotLogic(appPrivateSettings, publicSettings, this);
        
        
    }
    
    
    private class BotThread extends Thread {
        BotPrivateSettings botPrivateSettings;
        public BotThread(BotPrivateSettings botPrivateSettings) {
            this.botPrivateSettings = botPrivateSettings;
        }
        
        @Override
        public void run(){
            String deviceInfoFileName = botPrivateSettings.getBotAccount() + "_device.json";
            Bot miraiBot = BotFactory.INSTANCE.newBot(botPrivateSettings.getBotAccount(), botPrivateSettings.getBotPwd(), new BotConfiguration() {
                {
                    //保存设备信息到文件deviceInfo.json文件里相当于是个设备认证信息
                    fileBasedDeviceInfo(deviceInfoFileName);
                    setProtocol(MiraiProtocol.ANDROID_PHONE); // 切换协议
                    // 开启所有列表缓存
                    enableContactCache();
                }
            });
            miraiBot.login();
            bots.put(botPrivateSettings.getBotAccount(), miraiBot);
            miraiBot.join();
        }
    }
    
    public String login(long botAccount) {
        Collection<BotPrivateSettings> allBotPrivateSettings = appPrivateSettings.getBotPrivateSettingsMap().values();
        BotPrivateSettings targetBotPrivateSettings = null;
        for (BotPrivateSettings botPrivateSettings : allBotPrivateSettings) {
            if (botPrivateSettings.getBotAccount() == botAccount) {
                targetBotPrivateSettings = botPrivateSettings;
                break;
            }
        }
        
        if (targetBotPrivateSettings == null) {
            log.warn("botAccount = {} 未找到 BotPrivateSettings", botAccount);
            return "botAccount未找到 BotPrivateSettings";
        }
        
        
        
        if (bots.containsKey(botAccount)) {
            log.warn("botAccount = {} 已存在", botAccount);
            return"botAccount已存在";
        }
        
        // the new thread will blocked by Bot.join()
        Thread thread = new BotThread(targetBotPrivateSettings);
        thread.start();
        
        return "OK";
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
    public Collection<Bot> getBots() {
        return bots.values();
    }

    @Override
    public Bot getBot(long botId) {
        return bots.get(botId);
    }
}
