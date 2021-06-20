package com.hundun.mirai.bot.core.character;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.function.JapaneseFunction;
import com.hundun.mirai.bot.core.function.QuizHandler;
import com.hundun.mirai.bot.core.function.WeiboFunction;
import com.hundun.mirai.bot.core.parser.StatementType;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.bot.export.IConsole;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
public class ZacaMusume extends BaseCharacter {

    //@Value("${character.zacaMusume.listenWeiboUids:}")
    public String[] listenWeiboUids = new String[0];
    
    public ZacaMusume() {
        super("CHARACTER_ZACA_MUSUME");
    }

    WeiboFunction weiboFunction;

    IConsole offlineConsole;
    
    QuizHandler quizHandler;
    
    JapaneseFunction japaneseFunction;
    
//    @Autowired
//    RepeatConsumer repeatConsumer;
    @Override
    public void manualWired() {
        super.manualWired();
        
        this.listenWeiboUids = CustomBeanFactory.getInstance().publicSettings.valueOrDefault(getId());
        
        this.weiboFunction = CustomBeanFactory.getInstance().weiboFunction;
        this.offlineConsole = CustomBeanFactory.getInstance().console;
        this.quizHandler = CustomBeanFactory.getInstance().quizHandler;
        this.japaneseFunction = CustomBeanFactory.getInstance().japaneseFunction;
    }
    
    @Override
    public void afterManualWired() {
        super.afterManualWired();
        
        weiboFunction.putCharacterToData(this.getId(), Arrays.asList(this.listenWeiboUids));
        
    }
    
    
    @Override
    protected void initParser() {


        registerWakeUpKeyword("ZACA娘");
        registerSubFunctionsByDefaultIdentifier(weiboFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(japaneseFunction.getSubFunctions());
        
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
            done = japaneseFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by japaneseFunction");
            }
        }
        if (!done) {
            done = guideFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by guideFunction");
            }
        }
        return done;
    }

    
    
    

}
