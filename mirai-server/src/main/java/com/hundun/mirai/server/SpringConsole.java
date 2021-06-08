package com.hundun.mirai.server;

import java.util.Arrays;
import java.util.List;

import com.hundun.mirai.bot.configuration.PrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogic;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.SimpleListenerHost;
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
public class SpringConsole implements IConsole {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";

    
    private Bot miraiBot;
    
    private List<Bot> bots = Arrays.asList((Bot) null);
    
    BotLogic botLogic;
    
    boolean isBotOnline = false;
    
    PrivateSettings privateSettings;
    
  //设备认证信息文件
    private final String deviceInfoPath = "device.json";
    
    
    public SpringConsole(PrivateSettings privateSettings, PublicSettings publicSettings) {
        this.privateSettings = privateSettings;
        
        this.botLogic = new BotLogic(privateSettings, publicSettings, this);
    }
    
    public void login() {
        if (null == privateSettings.getBotAccount() || null == privateSettings.getBotPwd()) {
            System.err.println("*****未配置账号或密码*****");
            log.warn("*****未配置账号或密码*****");
            return;
        }
        
        if (!isBotOnline) {
            // the new thread will blocked by Bot.join()
            Thread thread = new Thread(){
                @Override
                public void run(){
                    
                    
                    miraiBot = BotFactory.INSTANCE.newBot(privateSettings.getBotAccount(), privateSettings.getBotPwd(), new BotConfiguration() {
                        {
                            //保存设备信息到文件deviceInfo.json文件里相当于是个设备认证信息
                            fileBasedDeviceInfo(deviceInfoPath);
                            setProtocol(MiraiProtocol.ANDROID_PHONE); // 切换协议
                            // 开启所有列表缓存
                            enableContactCache();
                        }
                    });
                    miraiBot.login();
                    isBotOnline = true;
                    botLogic.onEnable();
                    bots = Arrays.asList(miraiBot);
                    miraiBot.join();
                }
            };
            thread.start();
        }
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
    public long getSelfAccount() {
        return privateSettings.getBotAccount();
    }

    @Override
    public Long getAdminAccount() {
        return privateSettings.getBotAccount();
    }
    
    @Override
    public List<Bot> getBots() {
        return bots;
    }
}
