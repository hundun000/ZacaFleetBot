package com.mirai.hundun.cp.penguin.domain;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(collection = "penguinStage")
public class Stage {
    @Id
    String stageId;
    String code;
    int apCost;
}
