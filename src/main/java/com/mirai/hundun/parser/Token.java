package com.mirai.hundun.parser;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/27
 */
@Data
public class Token {
    TokenType type;
    String textContent;
    Long longContent;
}
