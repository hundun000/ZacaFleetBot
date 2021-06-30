package com.hundun.mirai.bot.core.character;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.SettingManager;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.configuration.CharacterPublicSettings;
import com.hundun.mirai.bot.core.function.GuideFunction;
import com.hundun.mirai.bot.core.function.SubFunction;
import com.hundun.mirai.bot.core.parser.Parser;
import com.hundun.mirai.bot.core.parser.StatementType;
import com.hundun.mirai.bot.core.parser.TokenType;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.export.IMyEventHandler;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public abstract class BaseCharacter implements IMyEventHandler {
    
    @Autowired
    GuideFunction guideFunction;
    
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
    
    public boolean testOnGroupMessageEventMessage(EventInfo eventInfo) throws Exception {
        return this.onGroupMessageEvent(eventInfo);
    }
    
    public String getId() {
        return id;
    }

    
    protected void registerSubFunctionByCustomSetting(SubFunction subFunction, String customIdentifier) {
        
        
        this.parser.tokenizer.registerSubFunction(subFunction, customIdentifier);
        guideFunction.putDocument(this.getId(), subFunction, customIdentifier);
    }
    
    
    


    protected void registerSubFunctionsByDefaultIdentifier(List<SubFunction> subFunctions) {
        
        for (SubFunction subFunction : subFunctions) {

            this.parser.tokenizer.registerSubFunction(subFunction, subFunction.getDefaultIdentifier());
            guideFunction.putDocument(this.getId(), subFunction, null);
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
