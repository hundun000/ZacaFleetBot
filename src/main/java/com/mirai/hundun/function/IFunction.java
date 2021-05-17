package com.mirai.hundun.function;

import java.util.List;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction {
    boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    List<SubFunction> getSubFunctions();
    
    
}
