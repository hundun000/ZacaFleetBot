package com.hundun.mirai.bot.character;


import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.data.EventInfo;
import com.hundun.mirai.bot.data.SessionId;
import com.hundun.mirai.bot.export.CustomBeanFactory;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.function.QuizHandler;
import com.hundun.mirai.bot.parser.StatementType;
import com.hundun.mirai.bot.parser.statement.Statement;
import com.hundun.mirai.bot.parser.statement.SubFunctionCallStatement;

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


    IConsole offlineConsole;
    
    QuizHandler quizHandler;

    @Override
    public void manualWired() {
        super.manualWired();
        
        this.quizHandler = CustomBeanFactory.getInstance().quizHandler;
        this.offlineConsole = CustomBeanFactory.getInstance().console;
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
