package com.hundun.mirai.plugin.parser.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hundun.mirai.plugin.parser.Token;
import com.hundun.mirai.plugin.parser.TokenType;
import com.hundun.mirai.plugin.parser.statement.Statement;

import lombok.Getter;


/**
 * @author hundun
 * Created on 2021/04/27
 */
@Getter
public class LiteralValueStatement extends Statement {
    
    public static List<List<TokenType>> syntaxs = new ArrayList<>();
    static {
        syntaxs.add(Arrays.asList(TokenType.LITERAL_VALUE));
    }
    
    String value;
    
    public LiteralValueStatement(List<Token> tokens) {
        
        if (tokens.size() > 0) {
            StringBuilder builder = new StringBuilder();
            tokens.forEach(token -> builder.append(token.getTextContent()).append(" "));
            builder.setLength(builder.length() - 1);
            this.value = builder.toString();
        } else {
            this.value = "";
        }
        
        
    }
}
