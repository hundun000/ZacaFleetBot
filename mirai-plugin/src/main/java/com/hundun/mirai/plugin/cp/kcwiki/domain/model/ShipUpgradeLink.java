package com.hundun.mirai.plugin.cp.kcwiki.domain.model;
/**
 * @author hundun
 * Created on 2021/05/21
 */

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ShipUpgradeLink {
    List<Integer> upgradeLinkIds = new LinkedList<>();
    Map<Integer, ShipInfo> shipDetails = new LinkedHashMap<>();
}
