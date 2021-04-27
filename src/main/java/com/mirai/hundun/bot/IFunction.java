package com.mirai.hundun.bot;

import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.parser.statement.amiya.AmiyaFunctionCallStatement;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IFunction {
    boolean acceptStatement(GroupMessageEvent event, Statement statement);
}
