package com.mirai.hundun.character.function;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction {
    boolean acceptStatement(String sessionId, EventInfo eventinfo, Statement statement);
}
