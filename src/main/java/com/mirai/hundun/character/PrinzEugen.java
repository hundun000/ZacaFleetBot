package com.mirai.hundun.character;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.function.PrinzEugenChatFunction;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.TokenType;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class PrinzEugen extends BaseCharacter {

    
    public PrinzEugen() {
        super("PrinzEugen");
    }

    @Autowired
    PrinzEugenChatFunction chatFunction;
    
    @Override
    protected void initParser() {
        
    }

    @Override
    public boolean onNudgeEventMessage(@NotNull NudgeEvent event) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onGroupMessageEventMessage(@NotNull GroupMessageEvent event) throws Exception {
        Statement statement;
        try {
            statement = parser.simpleParse(event.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        String sessionId = getSessionId(event);
        boolean done = false;

        if (!done) {
            done = chatFunction.acceptStatement(sessionId, event, statement);
            if (done) {
                log.info("done by chatFunction");
            }
        }
        
        return done;
    }

}
