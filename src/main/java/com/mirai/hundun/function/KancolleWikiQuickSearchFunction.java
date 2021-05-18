package com.mirai.hundun.function;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.QuickSearchFunction.QuickSearchNode;
import com.mirai.hundun.parser.statement.QuickSearchStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Slf4j
@Component
public class KancolleWikiQuickSearchFunction implements IFunction {

    @Autowired
    BotService botService;
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof QuickSearchStatement) {
            QuickSearchStatement quickSearchStatement = (QuickSearchStatement)statement;


            String arg = quickSearchStatement.getMainArg();
            try {
                arg = URLEncoder.encode(
                        arg,
                        java.nio.charset.StandardCharsets.UTF_8.toString()
                      );
            } catch (UnsupportedEncodingException e) {
                log.warn("Urlencolde fail: {}", arg);
            }
            String answer = "https://zh.kcwiki.cn/wiki/" + arg;
            botService.sendToGroup(event.getGroupId(), answer);
            return true;
  
        }
        return false;
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return null;
    }

}
