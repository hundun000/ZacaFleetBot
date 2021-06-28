package com.hundun.mirai.bot.core.character;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.CustomBeanFactory;

import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.function.KancolleWikiQuickSearchFunction;
import com.hundun.mirai.bot.core.function.MiraiCodeFunction;
import com.hundun.mirai.bot.core.function.PrinzEugenChatFunction;
import com.hundun.mirai.bot.core.function.RepeatConsumer;
import com.hundun.mirai.bot.core.function.SubFunction;
import com.hundun.mirai.bot.core.function.WeiboFunction;
import com.hundun.mirai.bot.core.function.reminder.ReminderFunction;
import com.hundun.mirai.bot.core.parser.StatementType;
import com.hundun.mirai.bot.core.parser.statement.QuickSearchStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class PrinzEugen extends BaseCharacter {

    
    public PrinzEugen() {
        super("CHARACTER_PRINZ_EUGEN");
    }
    @Autowired
    WeiboFunction weiboFunction;
    @Autowired
    PrinzEugenChatFunction chatFunction;
    @Autowired
    RepeatConsumer repeatConsumer;
    @Autowired
    ReminderFunction reminderFunction;
    @Autowired
    MiraiCodeFunction miraiCodeFunction;
    @Autowired
    KancolleWikiQuickSearchFunction kancolleWikiQuickSearchFunction;
    
    @Override
    public void postConsoleBind() {
        super.postConsoleBind();

        
        weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());
        
        reminderFunction.addAllCharacterTasks(this.getId(), characterPublicSettings.getHourlyChats());


        
    }
    
    @Override
    protected void initParser() {
        registerWakeUpKeyword("欧根");
        registerQuickQueryKeyword(".");
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "查看镇守府情报");
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(miraiCodeFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        registerSyntaxs(QuickSearchStatement.syntaxs, StatementType.QUICK_SEARCH);
    }

    @Override
    public boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception {
        // TODO Auto-generated method stub
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
            done = reminderFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by reminderFunction");
            }
        }
        if (!done) {
            done = weiboFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by weiboFunction");
            }
        }
        if (!done) {
            done = miraiCodeFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by decodeFunction");
            }
        }
        if (!done) {
            done = chatFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by chatFunction");
            }
        }
        
        if (!done) {
            done = repeatConsumer.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by repeatConsumer");
            }
        }
        if (!done) {
            done = guideFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by guideFunction");
            }
        }
        if (!done) {
            done = kancolleWikiQuickSearchFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by kancolleWikiQuickSearchFunction");
            }
        }
        return done;
    }

    

}
