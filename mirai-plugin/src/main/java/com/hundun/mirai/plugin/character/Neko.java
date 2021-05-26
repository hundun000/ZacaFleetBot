package com.hundun.mirai.plugin.character;


import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.SessionId;
import com.hundun.mirai.plugin.function.QuizHandler;
import com.hundun.mirai.plugin.parser.StatementType;
import com.hundun.mirai.plugin.parser.statement.Statement;
import com.hundun.mirai.plugin.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.plugin.service.BotService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
public class Neko extends BaseCharacter {


    
    public Neko() {
        super("neko");
    }


    BotService botService;
    
    QuizHandler quizHandler;

    @Override
    public void manualWired() {
        super.manualWired();
        
        this.quizHandler = CustomBeanFactory.getInstance().quizHandler;
        this.botService = CustomBeanFactory.getInstance().botService;
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
