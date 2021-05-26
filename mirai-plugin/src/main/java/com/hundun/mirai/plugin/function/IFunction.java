package com.hundun.mirai.plugin.function;

import java.util.List;

import com.hundun.mirai.plugin.IManualWired;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.SessionId;
import com.hundun.mirai.plugin.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction extends IManualWired {
    boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    List<SubFunction> getSubFunctions();
    
    
}
