package hundun.zacafleetbot.mirai.botlogic.core.character;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.function.JapaneseFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.QuizHandler;
import hundun.zacafleetbot.mirai.botlogic.core.function.WeiboFunction;
import hundun.zacafleetbot.mirai.botlogic.core.parser.StatementType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class ZacaMusume extends BaseCharacter {

    
    public ZacaMusume() {
        super("CHARACTER_ZACA_MUSUME");
    }
    @Autowired
    WeiboFunction weiboFunction;
        
    @Autowired
    QuizHandler quizHandler;
    @Autowired
    JapaneseFunction japaneseFunction;
    
//    @Autowired
//    RepeatConsumer repeatConsumer;
    @Override
    public void postConsoleBind() {
        super.postConsoleBind();

        weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());

        
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
