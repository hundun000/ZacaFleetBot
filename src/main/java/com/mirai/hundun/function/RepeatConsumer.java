package com.mirai.hundun.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.core.EventInfo;
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
    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList();
    }
    //private final long groupId;
    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
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

    

    @Override
    public boolean acceptStatement(String sessionId, EventInfo event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.isEmpty()) {
                return false;
            }
            
            
            SessionData sessionData = sessionDataMap.get(sessionId);
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId, sessionData);
            } 
            
                
            if (sessionData.message.equals(newMessage)) {
                sessionData.count++;
            } else {
                sessionData.count = 1;
                sessionData.message = newMessage;
            }

            
            if (sessionData.count == 3) {
                botService.sendToGroup(event.getGroupId(), sessionData.message);
                return true;
            }
        }
        return false;
    }

}
