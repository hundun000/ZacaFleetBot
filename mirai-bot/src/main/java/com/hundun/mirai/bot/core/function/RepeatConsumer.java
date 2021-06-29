package com.hundun.mirai.bot.core.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.CustomBeanFactory;

import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.LiteralValueStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.code.MiraiCode;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Component
public class RepeatConsumer extends BaseFunction {
        

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
                console.sendToGroup(event.getBot(), event.getGroupId(), MiraiCode.deserializeMiraiCode(sessionData.messageMiraiCode));
                return true;
            }
        }
        return false;
    }

}
