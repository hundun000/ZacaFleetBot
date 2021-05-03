package com.mirai.hundun.cp.penguin.domain.report;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mirai.hundun.cp.penguin.domain.DropType;

import lombok.Data;

/**
 * 根据使用需求，自定义的报告结构
 * @author hundun
 * Created on 2021/04/29
 */
@Document(collection = "penguinStageInfoReport")
@Data
public class StageInfoReport {
    @Id
    String stageCode;
    int apCost;
    Map<DropType, List<StageInfoNode>> nodes;
    
}
