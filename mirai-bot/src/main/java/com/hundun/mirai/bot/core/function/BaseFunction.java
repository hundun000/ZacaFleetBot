package com.hundun.mirai.bot.core.function;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.export.IConsole;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Component
public abstract class BaseFunction  {
    
    @Autowired
    protected IConsole console;
    
    public abstract boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    public abstract List<SubFunction> getSubFunctions();
    
    
}
