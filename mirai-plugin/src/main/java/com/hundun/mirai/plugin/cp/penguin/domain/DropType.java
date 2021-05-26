package com.hundun.mirai.plugin.cp.penguin.domain;
/**
 * @author hundun
 * Created on 2021/04/29
 */
public enum DropType {
    NORMAL_DROP("常规掉落"), EXTRA_DROP("额外掉落"), SPECIAL_DROP("特殊掉落"), FURNITURE("幸运掉落"), RECOGNITION_ONLY("");
   
    private String des;
    private DropType(String des) {
        this.des = des;
    }
    
    public String getDes() {
        return des;
    }
}
