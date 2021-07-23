package hundun.zacafleetbot.mirai.botlogic.core.character;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.SettingManager;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.PreHandleablePrioritySelectorBTNode;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.CharacterPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.core.function.AmiyaChatFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.JapaneseFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.KancolleWikiQuickSearchFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.MiraiCodeFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.PenguinFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.PixivFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.PrinzEugenChatFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.QuickSearchFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.QuizHandler;
import hundun.zacafleetbot.mirai.botlogic.core.function.RepeatConsumer;
import hundun.zacafleetbot.mirai.botlogic.core.function.SubFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.WeiboFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.reminder.ReminderFunction;
import hundun.zacafleetbot.mirai.botlogic.core.parser.Parser;
import hundun.zacafleetbot.mirai.botlogic.core.parser.StatementType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.TokenType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.NudegeStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.cp.pixiv.PixivService;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public abstract class BaseCharacter extends PreHandleablePrioritySelectorBTNode {
    @Autowired
    protected IConsole console;
    @Autowired
    protected WeiboFunction weiboFunction;
    @Autowired
    protected AmiyaChatFunction amiyaChatFunction;
    @Autowired
    protected QuizHandler quizHandler;
    @Autowired
    protected PenguinFunction penguinFunction;
    @Autowired
    protected RepeatConsumer repeatConsumer;
    @Autowired
    protected ReminderFunction reminderFunction;
    @Autowired
    protected QuickSearchFunction quickSearchFunction;
    @Autowired
    protected MiraiCodeFunction miraiCodeFunction;
    @Autowired
    protected JapaneseFunction japaneseFunction;
    @Autowired
    protected KancolleWikiQuickSearchFunction kancolleWikiQuickSearchFunction;
    @Autowired
    protected PrinzEugenChatFunction prinzEugenChatFunction;
    @Autowired
    protected PixivFunction pixivFunction;
    
    @Autowired
    protected SettingManager settingManager;
    
    protected CharacterPublicSettings characterPublicSettings;
    
    protected final String id;
    
    public BaseCharacter(String id) {
        this.id = id;
    }
    
    
    private Parser parser = new Parser();
    
    @PostConstruct
    public void postConsoleBind() {

        this.characterPublicSettings = settingManager.getCharacterPublicSettings(getId());
        
        initParser();
    }
    
    protected abstract void initParser();
    

    public Statement testParse(String message) {
        Statement statement = parser.simpleParse(MessageUtils.newChain(new PlainText(message)));
        return statement;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        ProcessResult result = super.process(blackBoard);
        if (result.isDone()) {
            console.getLogger().info("done by character: " + getId() + " child: " + result.getProcessor().getClass().getSimpleName());
        }
        return result;
    }
    
    
    @Override
    public boolean selfEnable(BlackBoard blackBoard) {
        EventInfo eventInfo = blackBoard.getEvent();

        GroupConfig config = settingManager.getGroupConfigOrEmpty(eventInfo.getBot().getId(), eventInfo.getGroupId());
        if (!config.getEnableCharacters().contains(this.getId())) {
            return false;
        }
        return super.selfEnable(blackBoard);
    }

    @Override
    public boolean modifyBlackBoardForChildren(BlackBoard blackBoard) {
        EventInfo eventInfo = blackBoard.getEvent();

        Statement statement;
        try {
            if (eventInfo.getMiraiEventClass() == GroupMessageEvent.class) {
                statement = parserSimpleParse(eventInfo.getMessage());
            } else if (eventInfo.getMiraiEventClass() == NudgeEvent.class) {
                statement = new NudegeStatement(eventInfo.getSenderId(), eventInfo.getTargetId());
            } else {
                throw new Exception("unsupported MiraiEventClass: " + eventInfo.getMiraiEventClass());
            }
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        SessionId sessionId = new SessionId(this.getId(), eventInfo.getGroupId());
 
        blackBoard.setSessionId(sessionId);
        blackBoard.setStatement(statement);
        return true;
    }


    
    protected void registerSubFunctionByCustomSetting(SubFunction subFunction, String customIdentifier) {
        
        
        this.parser.tokenizer.registerSubFunction(subFunction, customIdentifier);
        //guideFunction.putDocument(this.getId(), subFunction, customIdentifier);
    }
    
    
    


    protected void registerSubFunctionsByDefaultIdentifier(List<SubFunction> subFunctions) {
        
        for (SubFunction subFunction : subFunctions) {

            this.parser.tokenizer.registerSubFunction(subFunction, subFunction.getDefaultIdentifier());
            //guideFunction.putDocument(this.getId(), subFunction, null);
        }
    }
    
    protected void registerQuickQueryKeyword(String keyword) {
        try {
            this.parser.tokenizer.registerKeyword(keyword, TokenType.QUICK_SEARCH);
        } catch (Exception e) {
            log.error("registerQuickQueryKeyword fail:{}", e.getMessage());
        }
    }
    
    protected void registerWakeUpKeyword(String keyword) {
        try {
            this.parser.tokenizer.registerKeyword(keyword, TokenType.WAKE_UP);
        } catch (Exception e) {
            log.error("registerWakeUpKeyword fail:{}", e.getMessage());
        }
    }
    
    protected void registerSyntaxs(List<List<TokenType>> syntaxs, StatementType type) {
        this.parser.syntaxsTree.registerSyntaxs(syntaxs, type);
    }
    
    protected Statement parserSimpleParse(MessageChain messageChain) {
        return this.parser.simpleParse(messageChain);
    }


    
}
