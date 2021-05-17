package com.mirai.hundun.character;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.AmiyaChatFunction;
import com.mirai.hundun.function.PenguinFunction;
import com.mirai.hundun.function.QuickSearchFunction;
import com.mirai.hundun.function.QuizHandler;
import com.mirai.hundun.function.RepeatConsumer;
import com.mirai.hundun.function.SubFunction;
import com.mirai.hundun.function.WeiboFunction;
import com.mirai.hundun.function.reminder.ReminderFunction;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.QuickSearchStatement;
import com.mirai.hundun.parser.statement.SubFunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import lombok.extern.slf4j.Slf4j;

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
    QuickSearchFunction quickSearchFunction;
    
    @PostConstruct
    public void init() {
        
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
        
        
        registerWakeUpKeyword("阿米娅");
        registerQuickQueryKeyword(".");
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "看看饼");
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(penguinFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(reminderFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        registerSyntaxs(QuickSearchStatement.syntaxs, StatementType.QUICK_SEARCH);
        registerSyntaxs(AtStatement.syntaxs, StatementType.AT);
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
            statement = parserSimpleParse(eventInfo.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        SessionId sessionId = new SessionId(this.getId(), eventInfo.getGroupId());
 
        
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
            done = quickSearchFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by quickSearchFunction");
            }
        }
        if (!done) {
            done = guideFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by guideFunction");
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
