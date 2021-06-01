package com.hundun.mirai.bot.cp.penguin.domain.report;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@AllArgsConstructor
@Data
public class MatrixReportNode {
    public MatrixReportNode() {
    }
    /**
     * 掉落数
     */
    int quantity;
    
    /**
     * （出击）样本数
     */
    int times;
    
    /**
     * 掉落数/样本数 
     */
    double gainRate;
    String gainRateString;
    
    /**
     * 单件期望理智
     */
    double costExpectation;
    String costExpectationString;
    
    String stageCode;
    int stageCost;
    
    
}
