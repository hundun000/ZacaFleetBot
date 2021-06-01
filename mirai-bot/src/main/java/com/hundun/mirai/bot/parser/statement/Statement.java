package com.hundun.mirai.bot.parser.statement;

import java.util.List;

import com.hundun.mirai.bot.parser.StatementType;
import com.hundun.mirai.bot.parser.Token;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/27
 */
@Data
public abstract class Statement {
    List<Token> tokens;
    StatementType type;
    String originMiraiCode;
}