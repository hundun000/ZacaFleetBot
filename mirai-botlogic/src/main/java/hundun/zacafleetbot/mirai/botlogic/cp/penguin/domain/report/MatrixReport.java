package hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report;

import java.util.List;

import lombok.Data;

/**
 * 根据使用需求，自定义的报告结构
 * @author hundun
 * Created on 2021/04/26
 */
@Data
public class MatrixReport {

    String id;
    String itemName;
    List<MatrixReportNode> nodes;
    
}
