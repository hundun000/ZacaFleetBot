package com.hundun.mirai.bot.cp.penguin.domain;

import java.util.List;

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
@Document(collation = "penguinStage")
public class Stage {
    @Id
    String stageId;
    String code;
    int apCost;
    List<DropInfo> dropInfos;
}
