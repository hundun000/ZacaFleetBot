package com.hundun.mirai.server;

import com.hundun.mirai.bot.configuration.PrivateSettings;
import com.hundun.mirai.bot.service.IConsole;

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
    
    boolean isBotOnline = false;
    
    PrivateSettings privateSettings;
    
  //设备认证信息文件
    private final String deviceInfoPath = "device.json";
    
    
    public SpringConsole(PrivateSettings privateSettings) {
        this.privateSettings = privateSettings;
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
                    miraiBot.join();
                }
            };
            thread.start();
        }
    }

    @Override
    public void sendToGroup(Long groupId, String message) {
        if (isBotOnline) {
            miraiBot.getGroupOrFail(groupId).sendMessage(message);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, message = {}", groupId, message);
        }
    }

    @Override
    public void sendToGroup(long groupId, MessageChain messageChain) {
        if (isBotOnline) {
            miraiBot.getGroupOrFail(groupId).sendMessage(messageChain);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }

    @Override
    public Image uploadImage(Long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
            return miraiBot.getGroupOrFail(groupId).uploadImage(externalResource);
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
            return miraiBot.getGroupOrFail(groupId).uploadVoice(externalResource);
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
}
