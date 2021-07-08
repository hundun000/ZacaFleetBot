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
    
    
//    @Autowired
//    RepeatConsumer repeatConsumer;
    @Override
    public void postConsoleBind() {
        super.postConsoleBind();

        //weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());

        addChild(weiboFunction);
        addChild(japaneseFunction);
        addChild(quizHandler);
    }
    
    
    @Override
    protected void initParser() {


        registerWakeUpKeyword("ZACA娘");
        registerSubFunctionsByDefaultIdentifier(weiboFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(japaneseFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        
    }


}
