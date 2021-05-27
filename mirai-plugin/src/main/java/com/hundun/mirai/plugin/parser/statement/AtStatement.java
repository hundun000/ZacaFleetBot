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
public class AtStatement extends Statement {
    
    public static List<List<TokenType>> syntaxs = new ArrayList<>();
    static {
        syntaxs.add(Arrays.asList(TokenType.AT));
        syntaxs.add(Arrays.asList(TokenType.AT, TokenType.LITERAL_VALUE));
    }
    
    long target;
    
    public AtStatement(List<Token> tokens) {
        this.target = Long.valueOf(tokens.get(0).getTextContent());
    }
}