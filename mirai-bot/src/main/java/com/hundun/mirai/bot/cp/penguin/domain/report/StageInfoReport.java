package com.hundun.mirai.bot.cp.penguin.domain.report;

import java.util.List;
import java.util.Map;

import com.hundun.mirai.bot.cp.penguin.domain.DropType;

import lombok.Data;

/**
 * 根据使用需求，自定义的报告结构
 * @author hundun
 * Created on 2021/04/29
 */
@Data
public class StageInfoReport {
    String stageCode;
    int apCost;
    Map<DropType, List<StageInfoNode>> nodes;
}
