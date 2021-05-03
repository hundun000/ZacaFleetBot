package com.mirai.hundun.cp.penguin.domain.report;

import java.util.List;

import com.mirai.hundun.cp.penguin.domain.DropType;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/29
 */
@Data
public class StageInfoNode {
    String itemName;
    DropType dropType;
    int boundsLower;
    int boundsUpper;
}
