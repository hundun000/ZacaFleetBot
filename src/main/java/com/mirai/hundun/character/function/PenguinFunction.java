package com.mirai.hundun.character.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.cp.penguin.PenguinService;
import com.mirai.hundun.cp.penguin.domain.DropType;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReportNode;
import com.mirai.hundun.cp.penguin.domain.report.StageInfoNode;
import com.mirai.hundun.cp.penguin.domain.report.StageInfoReport;
import com.mirai.hundun.cp.quiz.Question;
import com.mirai.hundun.cp.quiz.QuizService;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class PenguinFunction implements IFunction {

    public String functionNameQueryResult = "查掉率";
    
    public String functionNameQueryStageInfo = "查作战";
    
    @Autowired
    PenguinService penguinService;
    
    @Autowired
    BotService botService;


    @Override
    public boolean acceptStatement(String sessionId, GroupMessageEvent event, Statement statement) {
        if (statement instanceof FunctionCallStatement) {
            FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
            if (functionCallStatement.getFunctionName().equals(functionNameQueryResult)) {
                String itemFuzzyName = functionCallStatement.getArgs().get(0);
                log.info("{} by {}", functionCallStatement.getFunctionName(), itemFuzzyName);
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
                    botService.sendToEventSubject(event, builder.toString());
                    
                } else {
                    botService.sendToEventSubject(event, "没找到“" + itemFuzzyName + "”的掉率QAQ");
                }
                return true;
            } else if (functionCallStatement.getFunctionName().equals(functionNameQueryStageInfo)) {
                String stageCode = functionCallStatement.getArgs().get(0);
                log.info("{} by {}", functionCallStatement.getFunctionName(), stageCode);
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
                    
                    botService.sendToEventSubject(event, builder.toString());
                } else {
                    botService.sendToEventSubject(event, "没找到“" + stageCode + "”的作战信息QAQ");
                }
                return true;
            }
            
        }
        return false;
    }

}
