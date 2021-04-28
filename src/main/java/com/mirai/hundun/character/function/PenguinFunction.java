package com.mirai.hundun.character.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.cp.penguin.PenguinService;
import com.mirai.hundun.cp.penguin.domain.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.MatrixReportNode;
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
    
    @Autowired
    PenguinService penguinService;
    
    @Autowired
    BotService botService;


    @Override
    public boolean acceptStatement(GroupMessageEvent event, Statement statement) {
        if (statement instanceof FunctionCallStatement) {
            FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
            if (functionCallStatement.getFunctionName().equals(functionNameQueryResult)) {
                String itemFuzzyName = functionCallStatement.getArgs().get(0);
                log.info("split itemFuzzyName = {}", itemFuzzyName);
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
            }
            
        }
        return false;
    }

}
