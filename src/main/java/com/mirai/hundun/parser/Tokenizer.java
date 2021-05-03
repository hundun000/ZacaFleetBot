package com.mirai.hundun.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;


/**
 * @author hundun
 * Created on 2021/04/27
 */
public class Tokenizer {

    public String KEYWORD_WAKE_UP = "UNSETTED";
    
    public Map<String, TokenType> keywords = new HashMap<>();
    public Set<String> functionNames = new HashSet<>();
    
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
                    } else if (functionNames.contains(subText)) {
                        Token token = new Token();
                        token.setType(TokenType.FUNCTION_NAME);
                        token.setTextContent(subText);
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
}
