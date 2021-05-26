package com.hundun.mirai.plugin.cp.penguin.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ResultMatrixResponse {
    
    
    List<ResultMatrixNode> matrix;
}
