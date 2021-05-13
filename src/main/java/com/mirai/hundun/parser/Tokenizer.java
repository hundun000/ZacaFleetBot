package com.mirai.hundun.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mirai.hundun.character.function.SubFunction;

import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;


/**
 * @author hundun
 * Created on 2021/04/27
 */
public class Tokenizer {

    private String KEYWORD_WAKE_UP = "UNSETTED";
    
    private Map<String, TokenType> keywords = new HashMap<>();
    private Map<String, SubFunction> functionIdentifiers = new HashMap<>();
    
    
    public Tokenizer() {
    }


    public List<Token> simpleTokenize(Message message) {
        List<Token> result = new ArrayList<>();
        if (message instanceof At) {
            At atMessage = (At)message;
            Token token = new Token();
            token.setType(TokenType.AT);
            token.setLongContent(atMessage.getTarget());
            result.add(token);
        } else if (message instanceof PlainText) {
            PlainText plainTextMessage = (PlainText)message;
            String text = plainTextMessage != null ? plainTextMessage.contentToString() : null;
            if (text != null && text.trim().length() > 0) {
                List<String> subTexts = new ArrayList<>(Arrays.asList(text.split(" ")));
                if (subTexts.get(0).startsWith(KEYWORD_WAKE_UP) && subTexts.get(0).length() > KEYWORD_WAKE_UP.length()) {
                    String autoSplit = subTexts.get(0).substring(KEYWORD_WAKE_UP.length());
                    subTexts.set(0, KEYWORD_WAKE_UP);
                    subTexts.add(1, autoSplit);
                }
                for (String subText : subTexts) {
                    if (keywords.containsKey(subText)) {
                        Token token = new Token();
                        token.setType(keywords.get(subText));
                        token.setTextContent(subText);
                        result.add(token);
                    } else if (functionIdentifiers.containsKey(subText)) {
                        SubFunction subFunction = functionIdentifiers.get(subText);
                        
                        Token token = new Token();
                        token.setType(TokenType.FUNCTION_NAME);
                        token.setTextContent(subFunction.name());
                        result.add(token);
                    } else {
                        Token token = new Token();
                        token.setType(TokenType.LITERAL_VALUE);
                        token.setTextContent(subText);
                        result.add(token);
                    }
                }
            }
        }
        return result;
    }
    
    public void registerWakeUpKeyword(String wakeUpKeyword) {
        this.KEYWORD_WAKE_UP = wakeUpKeyword;
        this.keywords.put(wakeUpKeyword, TokenType.WAKE_UP);
    }

    public void registerSubFunction(SubFunction subFunction, String customIdentifier) {
        functionIdentifiers.put(customIdentifier, subFunction);
    }
    
    public void registerSubFunctionsByDefaultIdentifier(List<SubFunction> subFunctions) {
        for (SubFunction subFunction : subFunctions) {
            functionIdentifiers.put(subFunction.getDefaultIdentifier(), subFunction);
        }
    }
}
