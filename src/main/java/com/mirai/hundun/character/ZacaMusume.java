package com.mirai.hundun.character;

import java.util.Arrays;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.function.QuizHandler;
import com.mirai.hundun.character.function.RepeatConsumer;
import com.mirai.hundun.character.function.SubFunction;
import com.mirai.hundun.character.function.WeiboFunction;
import com.mirai.hundun.core.EventInfo;
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


        parser.tokenizer.registerWakeUpKeyword("ZACA娘");
        parser.tokenizer.registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());

        
        parser.syntaxsTree.registerSyntaxs(FunctionCallStatement.syntaxs, StatementType.FUNCTION_CALL);
        
    }


    @Override
    public boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception {
        return false;
    }


    @Override
    public boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception {

        
        Statement statement;
        try {
            statement = parser.simpleParse(eventInfo.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        String sessionId = getSessionId(eventInfo);

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
        
        return done;
    }
    
    

}
