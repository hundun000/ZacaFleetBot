package com.mirai.hundun.character.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Slf4j
@Component
public class RepeatConsumer implements IFunction {

    @Autowired
    BotService botService;
    
    
    //private final long groupId;
    Map<Long, SessionData> groupIdToData = new HashMap<>();
    
    public class SessionData {
        public String message = "";
        public int count = 0;
    }
    public class A {
        
    }
    
    public RepeatConsumer() {
        //countNode = new CountNode();
        //this.groupId = groupId;
    }

    public boolean acceptPlainText(GroupMessageEvent event, PlainText plainText) {
        String newMessage = plainText != null ? plainText.contentToString() : null;
        
        if (newMessage == null) {
            return false;
        }
        long groupId = event.getGroup().getId();
        SessionData sessionData = groupIdToData.get(groupId);
        if (sessionData == null) {
            sessionData = new SessionData();
            groupIdToData.put(groupId, sessionData);
        } 
        
            
        if (sessionData.message.equals(newMessage)) {
            sessionData.count++;
        } else {
            sessionData.count = 1;
            sessionData.message = newMessage;
        }

        
        if (sessionData.count == 3) {
            //countNode.count = 0;
            botService.sendToEventSubject(event, sessionData.message);
            //log.info("RepeatConsumer update sendMessage, groupId = {}, sendMessage = {}", groupId, countNode.message);
        }
            
            
            //log.info("RepeatConsumer update Count, groupId = {}, sneder = {}, newCount = {}", groupId, countNode.count);
        return true;
    }

    @Override
    public boolean acceptStatement(GroupMessageEvent event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            long groupId = event.getGroup().getId();
            SessionData sessionData = groupIdToData.get(groupId);
            if (sessionData == null) {
                sessionData = new SessionData();
                groupIdToData.put(groupId, sessionData);
            } 
            
                
            if (sessionData.message.equals(newMessage)) {
                sessionData.count++;
            } else {
                sessionData.count = 1;
                sessionData.message = newMessage;
            }

            
            if (sessionData.count == 3) {
                botService.sendToEventSubject(event, sessionData.message);
                return true;
            }
        }
        return false;
    }

}
