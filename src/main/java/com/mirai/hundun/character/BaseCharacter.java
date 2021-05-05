package com.mirai.hundun.character;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.parser.Parser;
import com.mirai.hundun.parser.statement.Statement;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Component
public abstract class BaseCharacter {
    
    protected final String id;
    
    public BaseCharacter(String id) {
        this.id = id;
    }
    
    
    Parser parser = new Parser();
    
    @PostConstruct
    private void initBase() {
        initParser();
       
    }
    
    
    protected abstract void initParser();
    
    public abstract boolean onNudgeEvent(@NotNull EventInfo event) throws Exception;
    public abstract boolean onGroupMessageEvent(@NotNull EventInfo event) throws Exception;

    public Statement testParse(String message) {
        Statement statement = parser.simpleParse(MessageUtils.newChain(new PlainText(message)));
        return statement;
    }
    
    public boolean testOnGroupMessageEventMessage(EventInfo eventInfo) throws Exception {
        return this.onGroupMessageEvent(eventInfo);
    }
    
    public String getId() {
        return id;
    }
    
    protected String getSessionId(@NotNull EventInfo event) {
        return this.getId() + "@" + event.getGroupId();
    }
    
    
}
