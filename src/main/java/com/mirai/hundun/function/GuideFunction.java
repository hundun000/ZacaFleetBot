package com.mirai.hundun.function;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.parser.statement.SubFunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * SubFunctionDocument toText 可读性差，不适合用作用户手册功能。用户手册还得手写。
 * 
 * @author hundun
 * Created on 2021/05/17
 */
@Slf4j
@Component
public class GuideFunction implements IFunction {

    
    private Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    @Data
    private class SessionData {
        
        Map<SubFunction, SubFunctionDocument> subFunctionDocuments = new LinkedHashMap<>();
        Map<String, SubFunction> identifierToSubFunction = new HashMap<>();
    }
    
    @Autowired
    BotService botService;
    
    
    
    

    
    /*
     * SubFunctionDocument toText 可读性差，不适合用作用户手册功能。
     * 
     * 
     */
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.GUIDE_QUERY_FUNCTION_DOCUMENT) {
                SessionData sessionData = sessionDataMap.get(sessionId.getCharacterId());
                
                
                if (sessionData != null) {
                    if (subFunctionCallStatement.getArgs().size() != 0) {
                        // show targetFunction detail 
                        String subFunctionIdentifier = subFunctionCallStatement.getArgs().get(0);
                        SubFunction targetFunction = sessionData.getIdentifierToSubFunction().get(subFunctionIdentifier);
                        if (targetFunction == null) {
                            botService.sendToGroup(eventinfo.getGroupId(), "“" + subFunctionIdentifier + "”不是一个有效的指令id。");
                            return true;
                        }
                        SubFunctionDocument document = sessionData.getSubFunctionDocuments().get(targetFunction);
                        if (document != null) {
                            botService.sendToGroup(eventinfo.getGroupId(), document.toDetailText());
                            return true;
                        } else {
                            botService.sendToGroup(eventinfo.getGroupId(), "该功能找不到帮助。");
                            return true;
                        }
                    } else {
                        // show all Function names
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Entry<SubFunction, SubFunctionDocument> entry : sessionData.subFunctionDocuments.entrySet()) {
                            SubFunctionDocument document = entry.getValue();
                            if (entry.getKey() == SubFunction.GUIDE_QUERY_FUNCTION_DOCUMENT) {
                                stringBuilder.insert(0, document.toDetailText() + "\n\n");
                            } else {
                                stringBuilder.append(document.toSimpleText()).append("\n");
                            }
                            
                        }
                        botService.sendToGroup(eventinfo.getGroupId(), stringBuilder.toString());
                        return true;
                    }
                } else {
                    botService.sendToGroup(eventinfo.getGroupId(), "该角色找不到帮助。");
                    return true;
                }
            }
            
            
        }
        return false;
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.GUIDE_QUERY_FUNCTION_DOCUMENT);
    }

    public void putDocument(String characterId, SubFunction subFunction, String customIdentifier) {
        SubFunctionDocument document;
        try {
            document = subFunction.getDefaultSubFunctionDocument();
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }
        if (customIdentifier != null) {
            document.setSubFunctionIdentifier(customIdentifier);
        }
        
        if (!sessionDataMap.containsKey(characterId)) {
            sessionDataMap.put(characterId, new SessionData());
        }
        sessionDataMap.get(characterId).getSubFunctionDocuments().put(subFunction, document);
        sessionDataMap.get(characterId).getIdentifierToSubFunction().put(document.getSubFunctionIdentifier(), subFunction);
    }

}
