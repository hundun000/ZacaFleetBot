package com.mirai.hundun.cp.penguin.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
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
