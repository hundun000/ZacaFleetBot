package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.PenguinService;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.DropType;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report.MatrixReport;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report.MatrixReportNode;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report.StageInfoNode;
import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report.StageInfoReport;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Component
public class PenguinFunction extends BaseFunction {

    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(
                SubFunction.PENGUIN_QUERY_ITEM_DROP_RATE,
                SubFunction.PENGUIN_QUERY_STAGE_INFO,
                SubFunction.PENGUIN_UPDATE
                
                );
    }
    @Autowired
    PenguinService penguinService;


    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_QUERY_ITEM_DROP_RATE) {
                String itemFuzzyName = subFunctionCallStatement.getArgs().get(0);
                console.getLogger().info(subFunctionCallStatement.getSubFunction() + " by " + itemFuzzyName);
                MatrixReport report = penguinService.getTopResultNode(itemFuzzyName, 3);
                if (report != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(report.getItemName()).append("??????????????????\n");
                    builder.append("??????\t").append("?????????\t").append("??????????????????\n");
                    for (MatrixReportNode node : report.getNodes()) {
                        builder.append(node.getStageCode()).append("\t");
                        builder.append(node.getGainRateString()).append("\t");
                        builder.append(node.getCostExpectationString()).append("\n");
                    }
                    console.sendToGroup(event.getBot(), event.getGroupId(), builder.toString());
                    
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), "????????????" + itemFuzzyName + "????????????QAQ");
                }
                return new ProcessResult(this, true);
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_QUERY_STAGE_INFO) {
                String stageCode = subFunctionCallStatement.getArgs().get(0);
                console.getLogger().info(subFunctionCallStatement.getSubFunction() + " by " + stageCode);
                StageInfoReport report = penguinService.getStageInfoReport(stageCode);
                if (report != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("??????").append(report.getStageCode()).append("????????????\n");
                    builder.append("???????????????").append(report.getApCost()).append("\n");
                    
                    List<StageInfoNode> nodes;
                    
                    nodes = report.getNodes().get(DropType.NORMAL_DROP);
                    if (nodes != null && nodes.size() > 0) {
                        builder.append(DropType.NORMAL_DROP.getDes()).append("???");
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
                        builder.append(DropType.EXTRA_DROP.getDes()).append("???");
                        for (StageInfoNode node : nodes) {
                            builder.append(node.getItemName()).append(", ");
                        }
                        builder.setLength(builder.length() - ", ".length());
                        builder.append("\n");
                    }
                    
                    console.sendToGroup(event.getBot(), event.getGroupId(), builder.toString());
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), "????????????" + stageCode + "??????????????????QAQ");
                }
                return new ProcessResult(this, true);
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.PENGUIN_UPDATE) {
                penguinService.resetCache();
                console.sendToGroup(event.getBot(), event.getGroupId(), "??????");
                return new ProcessResult(this, true);
            }
        }
        return new ProcessResult(this, false);
    }

}
