package com.mirai.hundun.cp.kcwiki.domain.model;

import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiInitEquip;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipDetail;
import com.mirai.hundun.cp.kcwiki.domain.dto.KcwikiShipStats;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/24
 */
@Data
public class ShipInfo {
    
    int id;
    String name;
    int afterLv;
    int afterShipId;
    String chineseName;
    String stypeNameChinese;
    KcwikiShipStats stats;
    KcwikiInitEquip initEquip;
    
    public ShipInfo(KcwikiShipDetail detail, KcwikiInitEquip initEquip) {
        this.id = detail.getId();
        this.name = detail.getName();
        this.afterLv = detail.getAfter_lv();
        this.afterShipId = detail.getAfter_ship_id();
        this.chineseName = detail.getChinese_name();
        this.stypeNameChinese = detail.getStype_name_chinese();
        this.stats = detail.getStats();
        this.initEquip = initEquip;
    }
    
    public String toSimpleText() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name);
        builder.append("[").append(this.stypeNameChinese).append("]");
        if (this.initEquip != null && initEquip.getSlot_names() != null && !initEquip.getSlot_names().isEmpty()) {
            builder.append("(");
            for (String slotName : initEquip.getSlot_names()) {
                builder.append(slotName).append(",");
            }
            builder.setLength(builder.length() - 1);
            builder.append(")");
        }
        return builder.toString();
    }
}
