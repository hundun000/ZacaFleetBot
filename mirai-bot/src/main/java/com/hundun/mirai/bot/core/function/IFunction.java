package com.hundun.mirai.bot.core.function;

import java.util.List;

import com.hundun.mirai.bot.core.IPostConsoleBind;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction extends IPostConsoleBind {
    boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    List<SubFunction> getSubFunctions();
    
    
}
