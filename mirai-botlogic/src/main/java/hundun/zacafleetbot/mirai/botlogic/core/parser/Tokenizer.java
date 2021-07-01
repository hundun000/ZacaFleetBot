package hundun.zacafleetbot.mirai.botlogic.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.zacafleetbot.mirai.botlogic.core.function.SubFunction;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;


/**
 * @author hundun
 * Created on 2021/04/27
 */
public class Tokenizer {

    private String KEYWORD_WAKE_UP = "UNSETTED";
    private String KEYWORD_QUICK_QUERRY = "UNSETTED";
    
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
            token.setExtraContent(String.valueOf(atMessage.getTarget()));
            result.add(token);
        } else if (message instanceof PlainText) {
            PlainText plainTextMessage = (PlainText)message;
            String text = plainTextMessage != null ? plainTextMessage.contentToString() : null;
            if (text != null && text.trim().length() > 0) {
                List<String> subTexts = new ArrayList<>(Arrays.asList(text.split(" ")));
                // special rule: split WAKE_UP from start
                if (subTexts.get(0).startsWith(KEYWORD_WAKE_UP) && subTexts.get(0).length() > KEYWORD_WAKE_UP.length()) {
                    String autoSplit = subTexts.get(0).substring(KEYWORD_WAKE_UP.length());
                    subTexts.set(0, KEYWORD_WAKE_UP);
                    subTexts.add(1, autoSplit);
                }
                // special rule: split WAKE_UP from start
                if (subTexts.get(0).endsWith(KEYWORD_QUICK_QUERRY) && subTexts.get(0).length() > KEYWORD_QUICK_QUERRY.length()) {
                    String autoSplit = subTexts.get(0).substring(0, subTexts.get(0).length() - KEYWORD_QUICK_QUERRY.length());
                    subTexts.set(0, autoSplit);
                    subTexts.add(1, KEYWORD_QUICK_QUERRY);
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
                        token.setTextContent(subText);
                        token.setExtraContent(subFunction.name());
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

    
    public void registerKeyword(String keyword, TokenType tokenType) throws Exception {
        if (keywords.containsKey(keyword)) {
            throw new Exception("已存在keyword = " + keywords.get(keyword));
        }
        this.keywords.put(keyword, tokenType);
        if (tokenType == TokenType.WAKE_UP) {
            this.KEYWORD_WAKE_UP = keyword;
        } else if (tokenType == TokenType.QUICK_SEARCH) {
            this.KEYWORD_QUICK_QUERRY = keyword;
        }
    }

    public void registerSubFunction(SubFunction subFunction, String customIdentifier) {
        functionIdentifiers.put(customIdentifier, subFunction);
    }

}
