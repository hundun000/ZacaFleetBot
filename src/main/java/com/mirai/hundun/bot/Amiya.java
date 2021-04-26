package com.mirai.hundun.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.bot.RepeatConsumer.CountNode;
import com.mirai.hundun.bot.amiya.AmiyaTalkHandler;
import com.mirai.hundun.bot.amiya.AmiyaWeiboHandler;
import com.mirai.hundun.bot.amiya.PenguinHandler;
import com.mirai.hundun.bot.amiya.QuizHandler;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Component
public class Amiya implements Consumer<GroupMessageEvent> {
    
    public boolean enable = true;
    
    @Value("${account.group.arknights}")
    public long arknightsGroupId;
    
    @Autowired
    BotService botService;
    
    @Autowired
    AmiyaWeiboHandler arknightsWeiboHandler;
    
    @Autowired
    AmiyaTalkHandler amiyaTalkHandler;
    
    @Autowired
    QuizHandler quizHandler;
    
    @Autowired
    PenguinHandler penguinHandler;
    
    private boolean acceptAtByAllComponents(GroupMessageEvent event) {
        At at = (At) event.getMessage().stream().filter(At.class::isInstance).findFirst().orElse(null);
        boolean done = amiyaTalkHandler.acceptAt(event, at);
        
        return done;
    }
    
    private boolean acceptPlainTextByAllComponents(GroupMessageEvent event) {
        PlainText plainText = (PlainText) event.getMessage().stream().filter(PlainText.class::isInstance).findFirst().orElse(null);
        
        boolean done = arknightsWeiboHandler.acceptPlainText(event, plainText);
        if (!done) {
            done = amiyaTalkHandler.acceptPlainText(event, plainText);
        }
        if (!done) {
            done = quizHandler.acceptPlainText(event, plainText);
        }
        if (!done) {
            done = penguinHandler.acceptPlainText(event, plainText);
        }
        return done;
    }
    
    @Override
    public void accept(GroupMessageEvent event) {
        synchronized (this) {
            if (!enable || event.getSender().getId() == getSelfAccount()) {
                return;
            }
            
            long groupId = event.getGroup().getId();
            long senderId = event.getSender().getId();
            boolean done = false;
            if (!done) {
                done = acceptPlainTextByAllComponents(event);
            }
            if (!done) {
                done = acceptAtByAllComponents(event);
            }
        }
    }

    public long getSelfAccount() {
        return botService.getSelfAccount();
    }

    public void sendToArknightsGroup(String message) {
       botService.sendToGroup(arknightsGroupId, message);
    }
    
    
}
