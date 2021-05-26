package com.hundun.mirai.plugin.cp.penguin.domain;

import java.util.Map;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Item {
    String itemId;
    String sortId;
    String rarity;
    String name;
    Map<String, String[]> alias;
}
