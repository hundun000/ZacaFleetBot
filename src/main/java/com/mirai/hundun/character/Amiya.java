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
import com.mirai.hundun.character.function.reminder.ReminderFunction;
import com.mirai.hundun.configuration.TestSettings;
import com.mirai.hundun.core.EventInfo;
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

    @Value("${character.amiya.listenWeiboUids:}")
    public String[] listenWeiboUids;

    @Value("${character.amiya.everydayChatTasks:}")
    public String[] everydayChatTasks;
    
    @Autowired
    WeiboFunction weiboFunction;
    
    @Autowired
    AmiyaChatFunction amiyaChatFunction;
    
    @Autowired
    QuizHandler quizHandler;
    
    @Autowired
    PenguinFunction penguinFunction;
    
    @Autowired
    RepeatConsumer repeatConsumer;
    
    @Autowired
    ReminderFunction reminderFunction;
    
    @Autowired
    TestSettings settings;
    
    @PostConstruct
    public void init() {
        log.info("settings.list = {}",settings.list);
        
        weiboFunction.putCharacterToData(this.getId(), Arrays.asList(this.listenWeiboUids));

        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                19, 0, "十九点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                20, 0, "二十点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                21, 0, "二十一点。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                22, 0, "完全入夜了呢，二十二点到了。博士，您工作辛苦了。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                23, 0, "二十三点到了。有什么想喝的吗，博士？"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                0, 0, "呜哇！？正好0点！今天是，由阿米娅来担当助力的工作呢。我不会辜负大家的。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                1, 0, "凌晨一点到啦！凯尔希医生教导过我，工作的时候一定要保持全神贯注......嗯，全神贯注。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                9, 0, "九点到了。罗德岛全舰正处于通常航行状态。博士，整理下航程信息吧？"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                10, 0, "十点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                11, 0, "十一点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                12, 0, "十二点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                13, 0, "十三点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                14, 0, "十四点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                15, 0, "十五点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                16, 0, "十六点到了。欸嘿嘿......"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                17, 0, "十七点到了。博士，辛苦了！累了的话请休息一会儿吧。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                18, 0, "十八点到了。欸嘿嘿......"));
    }
    
    @Override
    protected void initParser() {
        
        
        parser.tokenizer.KEYWORD_WAKE_UP = "阿米娅";
        parser.tokenizer.keywords.put("阿米娅", TokenType.WAKE_UP);
        parser.tokenizer.functionNames.add(weiboFunction.functionName);
        parser.tokenizer.functionNames.add(quizHandler.functionNameNextQuest);
        parser.tokenizer.functionNames.add(quizHandler.functionNameStartMatch);
        parser.tokenizer.functionNames.add(penguinFunction.functionNameQueryResult);
        parser.tokenizer.functionNames.add(penguinFunction.functionNameQueryStageInfo);
        parser.tokenizer.functionNames.add(penguinFunction.functionNameUpdate);
        parser.tokenizer.functionNames.add(reminderFunction.functionCreateTask);
        
        parser.syntaxsTree.registerSyntaxs(FunctionCallStatement.syntaxs, StatementType.FUNCTION_CALL);
        parser.syntaxsTree.registerSyntaxs(AtStatement.syntaxs, StatementType.AT);
    }

    
    
    @Override
    public boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception {
        boolean done = amiyaChatFunction.acceptNudged(eventInfo);
        return done; 
    }

    @Override
    public boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception { // 可以抛出任何异常, 将在 handleException 处理


        if (!enable) {
            return false;
        }
        
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
            done = weiboFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by weiboFunction");
            }
        }
        if (!done) {
            done = reminderFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by reminderFunction");
            }
        }
        if (!done) {
            done = quizHandler.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by quizHandler");
            }
        }
        if (!done) {
            done = penguinFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by penguinHandler");
            }
        }
        if (!done) {
            done = repeatConsumer.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by repeatConsumer");
            }
        }
        if (!done) {
            done = amiyaChatFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by amiyaTalkHandler");
            }
        }
        
        return done;
    }


    
    




    
}
