package com.mirai.hundun.cp.penguin.domain;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(collection = "penguinItem")
public class Item {
    @Id
    String itemId;
    String sortId;
    String rarity;
    String name;
    Map<String, String[]> alias;
}
