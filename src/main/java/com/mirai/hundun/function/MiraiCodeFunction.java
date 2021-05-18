package com.mirai.hundun.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.RepeatConsumer.SessionData;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.parser.statement.SubFunctionCallStatement;
import com.mirai.hundun.service.BotService;

import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Component
public class MiraiCodeFunction implements IFunction {

    @Autowired
    BotService botService;

    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    public class SessionData {
        public String messageMiraiCode = "";
        
    }
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.DECODE_MIRAI_CODE) {
                if (event.getSenderId() == botService.getAdminAccount()) {
                    String miraiCode = subFunctionCallStatement.getArgs().get(0);
                    MessageChain chain = MiraiCode.deserializeMiraiCode(miraiCode);
                    botService.sendToGroup(event.getGroupId(), chain);
                    return true;
                } else {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return true;
                }     
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.ENCODE_LAST_TO_MIRAI_CODE) {
                SessionData sessionData = sessionDataMap.get(sessionId.id());
                if (sessionData == null) {
                    sessionData = new SessionData();
                    sessionDataMap.put(sessionId.id(), sessionData);
                } 
                String miraiCode = sessionDataMap.get(sessionId.id()).messageMiraiCode;
                botService.sendToGroup(event.getGroupId(), miraiCode);
                return true;
            }
        } else if (statement instanceof LiteralValueStatement) {
            SessionData sessionData = sessionDataMap.get(sessionId.id());
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId.id(), sessionData);
            } 
            sessionDataMap.get(sessionId.id()).messageMiraiCode = statement.getOriginMiraiCode();
        }
        return false;
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.DECODE_MIRAI_CODE,
                SubFunction.ENCODE_LAST_TO_MIRAI_CODE);
    }

}
