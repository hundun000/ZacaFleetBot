package com.mirai.hundun.character;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.function.QuizHandler;
import com.mirai.hundun.character.function.RepeatConsumer;
import com.mirai.hundun.parser.Parser;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.TokenType;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class ZacaMusume extends BaseCharacter {

    
    public ZacaMusume() {
        super("ZacaMusume");
    }


    @Autowired
    BotService botService;
    
    @Autowired
    QuizHandler quizHandler;
    
    @Autowired
    RepeatConsumer repeatConsumer;
    
    
    
    @Override
    protected void initParser() {


        parser.tokenizer.KEYWORD_WAKE_UP = "ZACA娘";
        parser.tokenizer.keywords.put("ZACA娘", TokenType.WAKE_UP);
        parser.tokenizer.functionNames.add(quizHandler.functionName);
        
        parser.syntaxsTree.registerSyntaxs(FunctionCallStatement.syntaxs, StatementType.FUNCTION_CALL);
        
    }


    @Override
    public boolean onNudgeEventMessage(@NotNull NudgeEvent event) throws Exception {
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
        
        
        boolean done = false;

        if (!done) {
            done = quizHandler.acceptStatement(event, statement);
            if (done) {
                log.info("done by quizHandler");
            }
        }
        if (!done) {
            done = repeatConsumer.acceptStatement(event, statement);
            if (done) {
                log.info("done by repeatConsumer");
            }
        }
        
        return done;
    }
    
    

}
