package hundun.zacafleetbot.mirai.botlogic.core.character;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.function.KancolleWikiQuickSearchFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.MiraiCodeFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.PrinzEugenChatFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.RepeatConsumer;
import hundun.zacafleetbot.mirai.botlogic.core.function.SubFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.WeiboFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.reminder.ReminderFunction;
import hundun.zacafleetbot.mirai.botlogic.core.parser.StatementType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.QuickSearchStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
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

    
    @Override
    public void postConsoleBind() {
        super.postConsoleBind();

        
        //weiboFunction.putCharacterToData(this.getId(), characterPublicSettings.getListenWeiboUids());
        
        reminderFunction.addAllCharacterTasks(this.getId(), characterPublicSettings.getHourlyChats());

        addChild(weiboFunction);
        addChild(reminderFunction);
        addChild(miraiCodeFunction);
        addChild(kancolleWikiQuickSearchFunction);
        addChild(prinzEugenChatFunction);
        addChild(repeatConsumer);
        addChild(pixivFunction);
    }
    
    @Override
    protected void initParser() {
        registerWakeUpKeyword("欧根");
        registerQuickQueryKeyword(".");
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "查看镇守府情报");
        registerSubFunctionsByDefaultIdentifier(miraiCodeFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(pixivFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        registerSyntaxs(QuickSearchStatement.syntaxs, StatementType.QUICK_SEARCH);
    }


}
