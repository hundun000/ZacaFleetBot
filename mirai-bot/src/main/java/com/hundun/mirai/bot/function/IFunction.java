package com.hundun.mirai.bot.function;

import java.util.List;

import com.hundun.mirai.bot.IManualWired;
import com.hundun.mirai.bot.data.EventInfo;
import com.hundun.mirai.bot.data.SessionId;
import com.hundun.mirai.bot.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction extends IManualWired {
    boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    List<SubFunction> getSubFunctions();
    
    
}
