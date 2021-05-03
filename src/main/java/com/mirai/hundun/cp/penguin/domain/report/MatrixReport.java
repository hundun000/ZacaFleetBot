package com.mirai.hundun.cp.penguin.domain.report;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 根据使用需求，自定义的报告结构
 * @author hundun
 * Created on 2021/04/26
 */
@Document(collection = "matrixReport")
@Data
public class MatrixReport {

    @Id
    String id;
    String itemName;
    List<MatrixReportNode> nodes;
    
}
