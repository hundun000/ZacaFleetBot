package com.mirai.hundun.character;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.QuizHandler;
import com.mirai.hundun.function.WeiboFunction;
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
public class ZacaMusume extends BaseCharacter {

    @Value("${character.zacaMusume.listenWeiboUids:}")
    public String[] listenWeiboUids;
    
    public ZacaMusume() {
        super("ZacaMusume");
    }

    @Autowired
    WeiboFunction weiboFunction;

    @Autowired
    BotService botService;
    
    @Autowired
    QuizHandler quizHandler;
    
//    @Autowired
//    RepeatConsumer repeatConsumer;
    
    @PostConstruct
    public void init() {
        weiboFunction.putCharacterToData(this.getId(), Arrays.asList(this.listenWeiboUids));
        
    }
    
    @Override
    protected void initParser() {


        registerWakeUpKeyword("ZACA娘");
        registerSubFunctionsByDefaultIdentifier(weiboFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        
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
//        if (!done) {
//            done = repeatConsumer.acceptStatement(sessionId, event, statement);
//            if (done) {
//                log.info("done by repeatConsumer");
//            }
//        }
        if (!done) {
            done = guideFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by guideFunction");
            }
        }
        return done;
    }
    
    

}
