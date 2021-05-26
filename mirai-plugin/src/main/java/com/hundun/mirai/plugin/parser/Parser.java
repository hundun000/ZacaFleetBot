package com.hundun.mirai.plugin.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundun.mirai.plugin.parser.statement.AtStatement;
import com.hundun.mirai.plugin.parser.statement.LiteralValueStatement;
import com.hundun.mirai.plugin.parser.statement.QuickSearchStatement;
import com.hundun.mirai.plugin.parser.statement.Statement;
import com.hundun.mirai.plugin.parser.statement.SubFunctionCallStatement;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;



/**
 * @author hundun
 * Created on 2021/04/27
 */
@Slf4j
public class Parser {

    public Tokenizer tokenizer = new Tokenizer();
    
    public SyntaxsTree syntaxsTree = new SyntaxsTree();
    
    public Parser() {
        
    }
    

    
    public Statement simpleParse(MessageChain messageChain) {
        
        
        List<Token> tokens = new ArrayList<>();
        for (Message message : messageChain) {
            List<Token> newTokens = tokenizer.simpleTokenize(message);
            tokens.addAll(newTokens);
        }
        
        StatementType type = syntaxsTree.root.accept(tokens, 0);
        if (type == null) {
            type = StatementType.SYNTAX_ERROR;
        }
        Statement statement;
        switch (type) {
            case AT:
                statement = new AtStatement(tokens);
                break;
            case SUB_FUNCTION_CALL:
                statement = new SubFunctionCallStatement(tokens);
                break;
            case QUICK_SEARCH:
                statement = new QuickSearchStatement(tokens);
                break;
            case SYNTAX_ERROR:
            default:
                type = StatementType.PLAIN_TEXT;
                statement = new LiteralValueStatement(tokens);
                break;
        }
        
        
        statement.setType(type);
        statement.setTokens(tokens);
        statement.setOriginMiraiCode(messageChain.serializeToMiraiCode());
        return statement;
    }
    
    public class SyntaxsTree {
        DFANode root = new DFANode();
        
        public void registerSyntaxs(List<List<TokenType>> grammars, StatementType type) {
            for (List<TokenType> grammar : grammars) {
                registerSyntax(grammar, type);
            }
        }
        
        public void registerSyntax(List<TokenType> grammar, StatementType type) {
            DFANode nowNode = root;

            for(int i = 0; i < grammar.size(); i++) {
                TokenType word = grammar.get(i);
                
                DFANode nextNode = nowNode.getChildNode(word);
                
                if (nextNode == null) {
                    nextNode = new DFANode();
                    nowNode.put(word, nextNode);
                }
                nowNode = nextNode;
                
                if (i == grammar.size() - 1) {
                    nowNode.endType = type;
                }
            }

            
        }
    }
    
    class DFANode {
        
        
        private Map<TokenType, DFANode> children;
        StatementType endType;
        
        public DFANode() {
            this.children = new HashMap<>();
            this.endType = null;
        }

        public DFANode getChildNode(TokenType input) {
            return children.get(input);
        }

        public void put(TokenType input, DFANode node) {
            if (children.containsKey(input)) {
                log.error("DFA node {} 已存在");
            }
            children.put(input, node);
        }
        
        /**
         * 特别地，当token的原type非accept时，会尝试把token类型改变为LITERAL_VALU再次检查
         */
        public StatementType accept(List<Token> tokens, int currentIndex) {
            if (tokens == null) {
                return null;
            }
            if (tokens.size() > currentIndex) {
                Token top = tokens.get(currentIndex);
                DFANode nextNode = getChildNode(top.getType());
                if (nextNode == null) {
                    nextNode = getChildNode(TokenType.LITERAL_VALUE);
                    if (nextNode == null) {
                        return StatementType.SYNTAX_ERROR;
                    } else {
                        top.setType(TokenType.LITERAL_VALUE);
                        top.setExtraContent(null);
                    }
                }
                return nextNode.accept(tokens, currentIndex + 1);
            } else {
                return endType;
            }
        }

        public int size() {
            return children.size();
        }
        
        public Set<TokenType> getKeySet(){ 
            Set<TokenType> set;
            set = children.keySet();
            return set;
        }
        
        @Override
        public String toString() {
            return children.toString();
        }
    }
    
    
}
