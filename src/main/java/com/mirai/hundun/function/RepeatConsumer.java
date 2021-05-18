package com.mirai.hundun.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.code.MiraiCode;

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
        public String messageMiraiCode = "";
        public int count = 0;
    }
    public class A {
        
    }
    
    public RepeatConsumer() {
        //countNode = new CountNode();
        //this.groupId = groupId;
    }

    

    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessageMiraiCode = ((LiteralValueStatement)statement).getOriginMiraiCode();


            SessionData sessionData = sessionDataMap.get(sessionId.id());
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId.id(), sessionData);
            } 
            
                
            if (sessionData.messageMiraiCode.equals(newMessageMiraiCode)) {
                sessionData.count++;
            } else {
                sessionData.count = 1;
                sessionData.messageMiraiCode = newMessageMiraiCode;
            }

            
            if (sessionData.count == 3) {
                botService.sendToGroup(event.getGroupId(), MiraiCode.deserializeMiraiCode(sessionData.messageMiraiCode));
                return true;
            }
        }
        return false;
    }

}
