package com.mirai.hundun.bot;

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
import com.mirai.hundun.bot.amiya.AmiyaTalkHandler;
import com.mirai.hundun.bot.amiya.AmiyaWeiboHandler;
import com.mirai.hundun.bot.amiya.PenguinHandler;
import com.mirai.hundun.bot.amiya.QuizHandler;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.parser.Parser;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.TokenType;
import com.mirai.hundun.parser.Tokenizer;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.parser.statement.amiya.AmiyaFunctionCallStatement;
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
public class Amiya extends SimpleListenerHost {
    
    public boolean enable = true;
    
    @Value("${account.group.arknights}")
    public long arknightsGroupId;
    
    @Autowired
    BotService botService;
    
    @Autowired
    AmiyaWeiboHandler arknightsWeiboHandler;
    
    @Autowired
    AmiyaTalkHandler amiyaTalkHandler;
    
    @Autowired
    QuizHandler quizHandler;
    
    @Autowired
    PenguinHandler penguinHandler;
    
    @Autowired
    RepeatConsumer repeatConsumer;
    
    Parser parser = new Parser();
    
    
    @PostConstruct
    private void initParser() {
        
        parser.tokenizer.KEYWORD_WAKE_UP = "阿米娅";
        parser.tokenizer.keywords.put("阿米娅", TokenType.WAKE_UP);
        parser.tokenizer.functionNames.add(arknightsWeiboHandler.functionName);
        parser.tokenizer.functionNames.add(quizHandler.functionName);
        parser.tokenizer.functionNames.add(penguinHandler.functionNameQueryResult);
        
        parser.syntaxsTree.registerSyntaxs(AmiyaFunctionCallStatement.syntaxs, StatementType.AMIYA_FUNCTION_CALL);
        parser.syntaxsTree.registerSyntaxs(AtStatement.syntaxs, StatementType.AT);
    }
    
    
    
    public Statement testParse(String message) {
        Statement statement = parser.simpleParse(MessageUtils.newChain(new PlainText(message)));
        return statement;
    }
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        // 处理事件处理时抛出的异常
        log.warn("事件处理时异常:", exception);
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception {
        amiyaTalkHandler.acceptNudged(event);
        return ListeningStatus.LISTENING; 
    }

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理

        synchronized (this) {
            if (!enable || event.getSender().getId() == getSelfAccount()) {
                return ListeningStatus.LISTENING;
            }
            
            Statement statement;
            try {
                statement = parser.simpleParse(event.getMessage());
            } catch (Exception e) {
                log.error("Parse error: ", e);
                return ListeningStatus.LISTENING;
            }
            
            
            boolean done = arknightsWeiboHandler.acceptStatement(event, statement);
            if (!done) {
                done = amiyaTalkHandler.acceptStatement(event, statement);
            }
            if (!done) {
                done = quizHandler.acceptStatement(event, statement);
            }
            if (!done) {
                done = penguinHandler.acceptStatement(event, statement);
            }
            if (!done) {
                done = repeatConsumer.acceptStatement(event, statement);
            }
        }
        
        return ListeningStatus.LISTENING;
    }

    public long getSelfAccount() {
        return botService.getSelfAccount();
    }

    public void sendToArknightsGroup(String message) {
       botService.sendToGroup(arknightsGroupId, message);
    }
    
    public void sendToEventSubject(GroupMessageEvent event, String message) {
        botService.sendToEventSubject(event, message);
    }

    public void sendToEventSubject(GroupMessageEvent event, MessageChain messageChain) {
        botService.sendToEventSubject(event, messageChain);
    }



    public void sendToEventSubject(Contact contact, MessageChain messageChain) {
        botService.sendToContact(contact, messageChain);
    }
    
}
