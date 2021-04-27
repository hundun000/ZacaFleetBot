package com.mirai.hundun.parser.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mirai.hundun.parser.Token;
import com.mirai.hundun.parser.TokenType;
import com.mirai.hundun.parser.statement.Statement;

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
        
        if (tokens.size() > 1) {
            StringBuilder builder = new StringBuilder();
            tokens.forEach(token -> builder.append(token.getTextContent()));
            this.value = builder.toString();
        } else if (tokens.size() == 1) {
            this.value = tokens.get(0).getTextContent();
        } else {
            this.value = "";
        }
        
        
    }
}
