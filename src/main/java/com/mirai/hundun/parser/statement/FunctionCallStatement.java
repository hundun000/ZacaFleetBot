package com.mirai.hundun.parser.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mirai.hundun.character.function.SubFunction;
import com.mirai.hundun.parser.Token;
import com.mirai.hundun.parser.TokenType;

import lombok.Getter;


/**
 * @author hundun
 * Created on 2021/04/27
 */
@Getter
public class FunctionCallStatement extends Statement {
    
    public static List<List<TokenType>> syntaxs = new ArrayList<>();
    static {
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
        syntaxs.add(Arrays.asList(TokenType.WAKE_UP, TokenType.FUNCTION_NAME, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE, TokenType.LITERAL_VALUE));
    }
    
    SubFunction functionName;
    List<String> args;
    
    public FunctionCallStatement(List<Token> tokens) {
        this.functionName = SubFunction.valueOf(tokens.get(1).getTextContent());
        int argsStartIndex = 2;
        this.args = new ArrayList<>(tokens.size() - argsStartIndex);
        for (int i = argsStartIndex; i < tokens.size(); i ++) {
            this.args.add(tokens.get(i).getTextContent());
        }
    }
}
