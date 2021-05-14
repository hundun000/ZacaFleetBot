package com.mirai.hundun.function;
/**
 * @author hundun
 * Created on 2021/05/14
 */
public enum SubFunction {
    WEIBO_SHOW_LATEST("查看最新微博"),
    
    QUIZ_EXIT("结束比赛"),
    QUIZ_UPDATE_TEAM("配置队伍"),
    QUIZ_NEXT_QUEST("出题"),
    QUIZ_USE_SKILL("使用技能"),
    QUIZ_START_ENDLESS_MATCH("开始无尽模式比赛"),
    QUIZ_START_PRE_MATCH("开始预赛模式比赛"),
    QUIZ_START_MAIN_MATCH("开始复赛模式比赛"),
    
    PENGUIN_QUERY_ITEM_DROP_RATE("查掉率"),
    PENGUIN_UPDATE("更新企鹅物流"),
    PENGUIN_QUERY_STAGE_INFO("查作战"),
    
    REMINDER_CREATE_TASK("创建提醒"),
    REMINDER_LIST_TASK("查看提醒"),
    REMINDER_REMOVE_TASK("移除提醒"),
    ;
    
    
    
    private String defaultIdentifier;
    
    private SubFunction(String defaultIdentifier) {
        this.defaultIdentifier = defaultIdentifier;
    }
    
    public String getDefaultIdentifier() {
        return defaultIdentifier;
    }
}
