package com.hundun.mirai.bot.core.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hundun.mirai.bot.core.CharacterRouter;
import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.LiteralValueStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Slf4j
public class MiraiCodeFunction implements IFunction {

    IConsole offlineConsole;
    @Override
    public void manualWired() {
        this.offlineConsole = CustomBeanFactory.getInstance().console;
        this.characterRouter = CustomBeanFactory.getInstance().characterRouter;
    }
    
    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    public class SessionData {
        public String messageMiraiCode = "";
        
    }
    
    CharacterRouter characterRouter;
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.DECODE_MIRAI_CODE) {
                if (event.getSenderId() == characterRouter.getAdminAccount(event.getBot().getId())) {
                    String miraiCode = subFunctionCallStatement.getArgs().get(0);
                    log.info("build MessageChain by miraiCode = {}", miraiCode);
                    MessageChain chain = MiraiCode.deserializeMiraiCode(miraiCode);
                    offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), chain);
                    return true;
                } else {
                    offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return true;
                }     
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.ENCODE_LAST_TO_MIRAI_CODE) {
                SessionData sessionData = sessionDataMap.get(sessionId.id());
                if (sessionData == null) {
                    sessionData = new SessionData();
                    sessionDataMap.put(sessionId.id(), sessionData);
                } 
                String miraiCode = sessionDataMap.get(sessionId.id()).messageMiraiCode;
                offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), miraiCode);
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