package com.hundun.mirai.bot.character;

import java.util.Arrays;


import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.data.EventInfo;
import com.hundun.mirai.bot.data.SessionId;
import com.hundun.mirai.bot.export.CustomBeanFactory;
import com.hundun.mirai.bot.function.AmiyaChatFunction;
import com.hundun.mirai.bot.function.MiraiCodeFunction;
import com.hundun.mirai.bot.function.PenguinFunction;
import com.hundun.mirai.bot.function.QuickSearchFunction;
import com.hundun.mirai.bot.function.QuizHandler;
import com.hundun.mirai.bot.function.RepeatConsumer;
import com.hundun.mirai.bot.function.SubFunction;
import com.hundun.mirai.bot.function.WeiboFunction;
import com.hundun.mirai.bot.function.reminder.ReminderFunction;
import com.hundun.mirai.bot.parser.StatementType;
import com.hundun.mirai.bot.parser.statement.AtStatement;
import com.hundun.mirai.bot.parser.statement.QuickSearchStatement;
import com.hundun.mirai.bot.parser.statement.Statement;
import com.hundun.mirai.bot.parser.statement.SubFunctionCallStatement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
public class Amiya extends BaseCharacter {
    
    public Amiya() {
        super("Amiya");
    }


    public boolean enable = true;

    //@Value("${character.amiya.listenWeiboUids:}")
    public String[] listenWeiboUids = new String[0];

    
    WeiboFunction weiboFunction;
    
    AmiyaChatFunction amiyaChatFunction;
    
    QuizHandler quizHandler;

    PenguinFunction penguinFunction;

    RepeatConsumer repeatConsumer;
    
    ReminderFunction reminderFunction;

    QuickSearchFunction quickSearchFunction;
    
    MiraiCodeFunction miraiCodeFunction;
    
    @Override
    public void manualWired() {
        super.manualWired();
        
        this.listenWeiboUids = CustomBeanFactory.getInstance().publicSettings.amiyaListenWeiboUids;
        
        this.weiboFunction = CustomBeanFactory.getInstance().weiboFunction;
        this.amiyaChatFunction = CustomBeanFactory.getInstance().amiyaChatFunction;
        this.quizHandler = CustomBeanFactory.getInstance().quizHandler;
        this.penguinFunction = CustomBeanFactory.getInstance().penguinFunction;
        this.repeatConsumer = CustomBeanFactory.getInstance().repeatConsumer;
        this.reminderFunction = CustomBeanFactory.getInstance().reminderFunction;
        this.quickSearchFunction = CustomBeanFactory.getInstance().quickSearchFunction;
        this.miraiCodeFunction = CustomBeanFactory.getInstance().miraiCodeFunction;
    }
    
    @Override
    public void afterManualWired() {
        super.afterManualWired();
        
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
                0, 0, "呜哇！？正好0点！今天是，由阿米娅来担当助理的工作呢。我不会辜负大家的。"));
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
        registerSubFunctionsByDefaultIdentifier(miraiCodeFunction.getSubFunctions());
        
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
            done = miraiCodeFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by decodeFunction");
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
