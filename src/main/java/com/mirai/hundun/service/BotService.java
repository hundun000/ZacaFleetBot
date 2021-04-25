package com.mirai.hundun.service;


import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;

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

import com.mirai.hundun.bot.Amiya;
import com.mirai.hundun.bot.RepeatConsumer;
import com.mirai.hundun.weibo.WeiboService;
import com.mirai.hundun.weibo.domain.WeiboCardCache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Slf4j
@Component
public class BotService {
    
    boolean isLogined = false;
    
    @Value("${account.bot.account}")
    public Long amiyAccount;
    @Value("${account.bot.pwd}")
    public String amiyPwd;

    @Autowired
    Amiya amiya;
    
    @Autowired
    WeiboService weiboService;
    

    
    
    private Bot amiyBot;
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
        if (null == amiyAccount || null == amiyPwd) {
            System.err.println("*****未配置账号或密码*****");
            log.warn("*****未配置账号或密码*****");
            return;
        }

        
        amiyBot = BotFactory.INSTANCE.newBot(amiyAccount, amiyPwd, new BotConfiguration() {
            {
                //保存设备信息到文件deviceInfo.json文件里相当于是个设备认证信息
                fileBasedDeviceInfo(deviceInfoPath);
                setProtocol(MiraiProtocol.ANDROID_PHONE); // 切换协议
                // 开启所有列表缓存
                enableContactCache();
            }
        });
        
        
        //repeaterListener = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, repeatConsumer);
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, amiya);
        
        
    }
    
    
    public void login() {
        if (!isLogined) {
            // the new thread will blocked by Bot.join()
            Thread thread = new Thread(){
                public void run(){
                    amiyBot.login();
                    isLogined = true;
                    amiyBot.join();
                }
            };
            thread.start();
        }
    }


    public void sendToGroup(long groupId, String message) {
        if (isLogined) {
            amiyBot.getGroupOrFail(groupId).sendMessage(message);
        } else {
            log.info("[not isLogined]sendToGroup groupId = {}, message = {}", groupId, message);
        }
    }


    public long getSelfAccount() {
        return amiyAccount;
    }
    
    
}
