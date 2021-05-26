package com.hundun.mirai.plugin.function;

import java.util.Arrays;
import java.util.List;


import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.SessionId;
import com.hundun.mirai.plugin.cp.penguin.PenguinService;
import com.hundun.mirai.plugin.cp.penguin.domain.DropType;
import com.hundun.mirai.plugin.cp.penguin.domain.report.MatrixReport;
import com.hundun.mirai.plugin.cp.penguin.domain.report.MatrixReportNode;
import com.hundun.mirai.plugin.cp.penguin.domain.report.StageInfoNode;
import com.hundun.mirai.plugin.cp.penguin.domain.report.StageInfoReport;
import com.hundun.mirai.plugin.parser.statement.Statement;
import com.hundun.mirai.plugin.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.plugin.service.BotService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
public class PenguinFunction implements IFunction {

    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(
                SubFunction.PENGUIN_QUERY_ITEM_DROP_RATE,
                SubFunction.PENGUIN_QUERY_STAGE_INFO,
                SubFunction.PENGUIN_UPDATE
                
                );
    }
    
    PenguinService penguinService;
    
    BotService botService;
    @Override
    public void manualWired() {
        this.botService = CustomBeanFactory.getInstance().botService;
        this.penguinService = CustomBeanFactory.getInstance().penguinService;
    }

    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_QUERY_ITEM_DROP_RATE) {
                String itemFuzzyName = subFunctionCallStatement.getArgs().get(0);
                log.info("{} by {}", subFunctionCallStatement.getSubFunction(), itemFuzzyName);
                MatrixReport report = penguinService.getTopResultNode(itemFuzzyName, 3);
                if (report != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(report.getItemName()).append("的掉率报告：\n");
                    builder.append("地图\t").append("掉落率\t").append("单件理智期望\n");
                    for (MatrixReportNode node : report.getNodes()) {
                        builder.append(node.getStageCode()).append("\t");
                        builder.append(node.getGainRateString()).append("\t");
                        builder.append(node.getCostExpectationString()).append("\n");
                    }
                    botService.sendToGroup(event.getGroupId(), builder.toString());
                    
                } else {
                    botService.sendToGroup(event.getGroupId(), "没找到“" + itemFuzzyName + "”的掉率QAQ");
                }
                return true;
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_QUERY_STAGE_INFO) {
                String stageCode = subFunctionCallStatement.getArgs().get(0);
                log.info("{} by {}", subFunctionCallStatement.getSubFunction(), stageCode);
                StageInfoReport report = penguinService.getStageInfoReport(stageCode);
                if (report != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("作战").append(report.getStageCode()).append("的报告：\n");
                    builder.append("理智消耗：").append(report.getApCost()).append("\n");
                    
                    List<StageInfoNode> nodes;
                    
                    nodes = report.getNodes().get(DropType.NORMAL_DROP);
                    if (nodes != null && nodes.size() > 0) {
                        builder.append(DropType.NORMAL_DROP.getDes()).append("：");
                        for (StageInfoNode node : nodes) {
                            builder.append(node.getItemName()).append("(");
                            builder.append(node.getBoundsLower()).append("~");
                            builder.append(node.getBoundsUpper()).append(")").append(", ");
                        }
                        builder.setLength(builder.length() - ", ".length());
                        builder.append("\n");
                    }
                    
                    nodes = report.getNodes().get(DropType.EXTRA_DROP);
                    if (nodes != null && nodes.size() > 0) {
                        builder.append(DropType.EXTRA_DROP.getDes()).append("：");
                        for (StageInfoNode node : nodes) {
                            builder.append(node.getItemName()).append(", ");
                        }
                        builder.setLength(builder.length() - ", ".length());
                        builder.append("\n");
                    }
                    
                    botService.sendToGroup(event.getGroupId(), builder.toString());
                } else {
                    botService.sendToGroup(event.getGroupId(), "没找到“" + stageCode + "”的作战信息QAQ");
                }
                return true;
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_UPDATE) {
                penguinService.resetCache();
                botService.sendToGroup(event.getGroupId(), "好的");
                return true;
            }
        }
        return false;
    }

}
