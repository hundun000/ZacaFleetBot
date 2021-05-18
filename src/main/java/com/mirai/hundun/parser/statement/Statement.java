package com.mirai.hundun.parser.statement;

import java.util.List;

import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.Token;

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
