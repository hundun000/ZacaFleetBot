package hundun.zacafleetbot.mirai.botlogic.core.character;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.function.AmiyaChatFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.MiraiCodeFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.PenguinFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.QuickSearchFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.QuizHandler;
import hundun.zacafleetbot.mirai.botlogic.core.function.RepeatConsumer;
import hundun.zacafleetbot.mirai.botlogic.core.function.SubFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.WeiboFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.reminder.ReminderFunction;
import hundun.zacafleetbot.mirai.botlogic.core.parser.StatementType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.AtStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.QuickSearchStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Slf4j
@Component
public class Amiya extends BaseCharacter {
    
    public Amiya() {
        super("CHARACTER_AMIYA");
    }

    
    @Override
    public void postConsoleBind() {
        super.postConsoleBind();
        
        //weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());

        reminderFunction.addAllCharacterTasks(this.getId(), characterPublicSettings.getHourlyChats());

        
        
        addChild(weiboFunction);
        addChild(quizHandler);
        addChild(penguinFunction);
        addChild(repeatConsumer);
        addChild(reminderFunction);
        addChild(quickSearchFunction);
        addChild(miraiCodeFunction);
        addChild(amiyaChatFunction);
        addChild(pixivFunction);
        
        
    }
    
    @Override
    protected void initParser() {
        
        
        registerWakeUpKeyword("阿米娅");
        registerQuickQueryKeyword(".");
        registerSubFunctionsByDefaultIdentifier(weiboFunction.getSubFunctions());
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "看看饼");
        registerSubFunctionsByDefaultIdentifier(quizHandler.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(penguinFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(reminderFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(miraiCodeFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(pixivFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        registerSyntaxs(QuickSearchStatement.syntaxs, StatementType.QUICK_SEARCH);
        registerSyntaxs(AtStatement.syntaxs, StatementType.AT);
    }

    
    
    


    

    


    
    




    
}
