package com.mirai.hundun.character;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.QuizHandler;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.statement.SubFunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class Neko extends BaseCharacter {


    
    public Neko() {
        super("neko");
    }


    @Autowired
    BotService botService;
    
    @Autowired
    QuizHandler quizHandler;

    
    @PostConstruct
    public void init() {

    }
    
    @Override
    protected void initParser() {


        registerWakeUpKeyword("猫猫");
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());

        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        
    }


    @Override
    public boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception {
        return false;
    }


    @Override
    public boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception {

        
        Statement statement;
        try {
            statement = parserSimpleParse(eventInfo.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        SessionId sessionId = new SessionId(this.getId(), eventInfo.getGroupId());

        boolean done = false;

        if (!done) {
            done = quizHandler.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by quizHandler");
            }
        }

        
        return done;
    }
    
    

}
