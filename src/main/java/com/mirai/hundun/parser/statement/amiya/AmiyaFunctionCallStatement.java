package com.mirai.hundun.parser.statement.amiya;

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
public class AmiyaFunctionCallStatement extends Statement {
    
    public static List<List<TokenType>> syntaxs = new ArrayList<>();
    static {
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
    }
    
    String functionName;
    List<String> args;
    
    public AmiyaFunctionCallStatement(List<Token> tokens) {
        this.functionName = tokens.get(1).getTextContent();
        int argsStartIndex = 2;
        this.args = new ArrayList<>(tokens.size() - argsStartIndex);
        for (int i = argsStartIndex; i < tokens.size(); i ++) {
            this.args.add(tokens.get(i).getTextContent());
        }
    }
}
