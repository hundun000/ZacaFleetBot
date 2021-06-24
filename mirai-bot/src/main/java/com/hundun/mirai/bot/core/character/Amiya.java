package com.hundun.mirai.bot.core.character;

import java.util.Arrays;


import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.data.configuration.CharacterPublicSettings;
import com.hundun.mirai.bot.core.function.AmiyaChatFunction;
import com.hundun.mirai.bot.core.function.MiraiCodeFunction;
import com.hundun.mirai.bot.core.function.PenguinFunction;
import com.hundun.mirai.bot.core.function.QuickSearchFunction;
import com.hundun.mirai.bot.core.function.QuizHandler;
import com.hundun.mirai.bot.core.function.RepeatConsumer;
import com.hundun.mirai.bot.core.function.SubFunction;
import com.hundun.mirai.bot.core.function.WeiboFunction;
import com.hundun.mirai.bot.core.function.reminder.ReminderFunction;
import com.hundun.mirai.bot.core.parser.StatementType;
import com.hundun.mirai.bot.core.parser.statement.AtStatement;
import com.hundun.mirai.bot.core.parser.statement.QuickSearchStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
public class Amiya extends BaseCharacter {
    
    public Amiya() {
        super("CHARACTER_AMIYA");
    }


    public boolean enable = true;


    
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
        
        weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());

        reminderFunction.addAllCharacterTasks(this.getId(), characterPublicSettings.getHourlyChats());
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
