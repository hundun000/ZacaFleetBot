package com.mirai.hundun.service;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.character.function.RepeatConsumer;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Slf4j
@Component
public class BotService {
    
    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";

    boolean isBotOnline = false;
    
    @Value("${account.admin.account}")
    public Long adminAccount;
    
    @Value("${account.bot.account}")
    public Long botAccount;
    @Value("${account.bot.pwd}")
    public String botPwd;

    @Autowired
    CharacterRouter characterRouter;
    
    private Bot miraiBot;
    //Listener<?> repeaterListener;
    //public RepeatConsumer repeatConsumer = new RepeatConsumer();
    
    @PostConstruct
    public void postConstruct() {
        this.init();
    }
    
    //设备认证信息文件
    private final String deviceInfoPath = "device.json";

    /**
     * 启动BOT
     */
    public void init() {
        if (null == botAccount || null == botPwd) {
            System.err.println("*****未配置账号或密码*****");
            log.warn("*****未配置账号或密码*****");
            return;
        }

        
        
        
        
        //repeaterListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, repeatConsumer);
        GlobalEventChannel.INSTANCE.registerListenerHost(characterRouter);
        
    }
    
    
    public void login() {
        if (!isBotOnline) {
            // the new thread will blocked by Bot.join()
            Thread thread = new Thread(){
                public void run(){
                    miraiBot = BotFactory.INSTANCE.newBot(botAccount, botPwd, new BotConfiguration() {
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

    public void sendToGroup(Long groupId, String message) {
        if (isBotOnline) {
            miraiBot.getGroupOrFail(groupId).sendMessage(message);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, message = {}", groupId, message);
        }
    }
    public void sendToGroup(long groupId, MessageChain messageChain) {
        if (isBotOnline) {
            miraiBot.getGroupOrFail(groupId).sendMessage(messageChain);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }


    public long getSelfAccount() {
        return botAccount;
    }
    
    public Long getAdminAccount() {
        return adminAccount;
    }


    public void sendToEventSubject(GroupMessageEvent event, String message) {
        if (message == null || message.isEmpty()) {
            log.warn("want send empty message “{}” to EventSubject", message);
            return;
        }
        if (isBotOnline) {
            event.getSubject().sendMessage(message);
        } else {
            log.info("[offline mode]sendToEventSubject message = {}", message);
        }
    }


    public void sendToEventSubject(GroupMessageEvent event, MessageChain messageChain) {
        if (isBotOnline) {
            event.getSubject().sendMessage(messageChain);
        } else {
            log.info("[offline mode]messageChain messageChain = {}", messageChain);
        }
    }


    public void sendToContact(Contact contact, MessageChain messageChain) {
        if (isBotOnline) {
            contact.sendMessage(messageChain);
        } else {
            log.info("[offline mode]sendToContact messageChain = {}", messageChain);
        }
    }


    public Image uploadImage(Long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
            return miraiBot.getGroupOrFail(groupId).uploadImage(externalResource);
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
        
    }


    
    
    
}
