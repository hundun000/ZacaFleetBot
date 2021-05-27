package com.hundun.mirai.plugin.parser.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hundun.mirai.plugin.parser.Token;
import com.hundun.mirai.plugin.parser.TokenType;

import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/05/18
 */
@Getter
public class QuickSearchStatement extends Statement {
    public static List<List<TokenType>> syntaxs = new ArrayList<>();
    static {
        syntaxs.add(Arrays.asList(TokenType.LITERAL_VALUE, TokenType.QUICK_SEARCH));
        syntaxs.add(Arrays.asList(TokenType.LITERAL_VALUE, TokenType.QUICK_SEARCH, TokenType.LITERAL_VALUE));
    }
    
    String mainArg;
    List<String> args;
    
    public QuickSearchStatement(List<Token> tokens) {
        this.mainArg = tokens.get(0).getTextContent();
        int argsStartIndex = 2;
        this.args = new ArrayList<>(tokens.size() - argsStartIndex);
        for (int i = argsStartIndex; i < tokens.size(); i ++) {
            this.args.add(tokens.get(i).getTextContent());
        }
    }
}
