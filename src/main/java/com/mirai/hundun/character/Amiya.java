package com.mirai.hundun.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.function.AmiyaChatFunction;
import com.mirai.hundun.character.function.WeiboFunction;
import com.mirai.hundun.character.function.PenguinFunction;
import com.mirai.hundun.character.function.QuizHandler;
import com.mirai.hundun.character.function.RepeatConsumer;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.parser.Parser;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.TokenType;
import com.mirai.hundun.parser.Tokenizer;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Component
public class Amiya extends BaseCharacter {
    
    public Amiya() {
        super("Amiya");
    }


    public boolean enable = true;
    
    

    
    public List<String> blogUids = Arrays.asList(WeiboService.yjUid, WeiboService.CHOSSHANLAND_UID);
    
    @Autowired
    WeiboFunction arknightsWeiboHandler;
    
    @Autowired
    AmiyaChatFunction amiyaChatFunction;
    
//    @Autowired
//    QuizHandler quizHandler;
    
    @Autowired
    PenguinFunction penguinFunction;
    
    @Autowired
    RepeatConsumer repeatConsumer;
    
    
    @Override
    protected void initParser() {
        
        
        parser.tokenizer.KEYWORD_WAKE_UP = "阿米娅";
        parser.tokenizer.keywords.put("阿米娅", TokenType.WAKE_UP);
        parser.tokenizer.functionNames.add(arknightsWeiboHandler.functionName);
        //parser.tokenizer.functionNames.add(quizHandler.functionName);
        parser.tokenizer.functionNames.add(penguinFunction.functionNameQueryResult);
        
        parser.syntaxsTree.registerSyntaxs(FunctionCallStatement.syntaxs, StatementType.FUNCTION_CALL);
        parser.syntaxsTree.registerSyntaxs(AtStatement.syntaxs, StatementType.AT);
    }

    
    

    public boolean onNudgeEventMessage(@NotNull NudgeEvent event) throws Exception {
        boolean done = amiyaChatFunction.acceptNudged(event);
        return done; 
    }


    public boolean onGroupMessageEventMessage(@NotNull GroupMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理


        if (!enable) {
            return false;
        }
        
        Statement statement;
        try {
            statement = parser.simpleParse(event.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        
        boolean done = arknightsWeiboHandler.acceptStatement(event, statement);
        if (!done) {
            done = amiyaChatFunction.acceptStatement(event, statement);
            if (done) {
                log.info("done by amiyaTalkHandler");
            }
        }
//        if (!done) {
//            done = quizHandler.acceptStatement(event, statement);
//            if (done) {
//                log.info("done by quizHandler");
//            }
//        }
        if (!done) {
            done = penguinFunction.acceptStatement(event, statement);
            if (done) {
                log.info("done by penguinHandler");
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
