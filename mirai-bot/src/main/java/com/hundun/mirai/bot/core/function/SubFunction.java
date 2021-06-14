package com.hundun.mirai.bot.core.function;
/**
 * @author hundun
 * Created on 2021/05/14
 */
public enum SubFunction {
    DECODE_MIRAI_CODE("解码"),
    ENCODE_LAST_TO_MIRAI_CODE("转码"),
    
    JAPANESE_TOOL("日文注音"),
    
    GUIDE_QUERY_FUNCTION_DOCUMENT("help"),
    
    WEIBO_SHOW_LATEST("查看最新微博"),
    
    QUIZ_EXIT("结束比赛"),
    QUIZ_UPDATE_TEAM("配置队伍"),
    QUIZ_NEXT_QUEST("出题"),
    QUIZ_USE_SKILL("使用技能"),
    QUIZ_START_MATCH("开始比赛"),

    
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
    
    public SubFunctionDocument getDefaultSubFunctionDocument() throws Exception {
        SubFunctionDocument document = new SubFunctionDocument();
        
        switch (this) {
            case GUIDE_QUERY_FUNCTION_DOCUMENT:
                document.setSubFunctionIdentifier("help");
                document.setSubFunctionDescription("查看指定id的指令的说明，或所有指令id");
                document.getParameterDescriptions().add("指令id（可选）；不填则显示所有指令id。");
                break;
            case WEIBO_SHOW_LATEST:
                document.setSubFunctionIdentifier("查看最新微博");
                document.setSubFunctionDescription("查看该角色订阅的微博最近一次更新时间。");
                break;
            
            case QUIZ_EXIT:
                document.setSubFunctionIdentifier("结束比赛");
                document.setSubFunctionDescription("答题功能指令。");
                break;
            case QUIZ_UPDATE_TEAM:
                document.setSubFunctionIdentifier("配置队伍");
                document.setSubFunctionDescription("答题功能指令。");
                break;
            case QUIZ_NEXT_QUEST:
                document.setSubFunctionIdentifier("出题");
                document.setSubFunctionDescription("答题功能指令。");
                break;
            case QUIZ_USE_SKILL:
                document.setSubFunctionIdentifier("使用技能");
                document.setSubFunctionDescription("答题功能指令。");
                break;
            case QUIZ_START_MATCH:
                document.setSubFunctionIdentifier("开始比赛");
                document.setSubFunctionDescription("答题功能指令。开始比赛。");
                document.getParameterDescriptions().add("比赛模式。值域：无尽模式、预赛模式、复赛模式。");
                document.getParameterDescriptions().add("题库名。举例：qustions。");
                document.getParameterDescriptions().add("所有参赛队伍名字。用“&”连接多个队名，举例：A队&B队&C队。");
                document.getParameterDescriptions().add("回复格式（可选）。值域：完整信息、简略信息。默认为简略信息。");
                break;
            case PENGUIN_QUERY_ITEM_DROP_RATE:
                document.setSubFunctionIdentifier("查掉率");
                document.setSubFunctionDescription("企鹅物流功能指令。查看某个物品的在所有地图里掉落率最高的几项。");
                document.getParameterDescriptions().add("物品名。举例：固源岩。");
                break;

            case PENGUIN_UPDATE:
                document.setSubFunctionIdentifier("更新企鹅物流");
                document.setSubFunctionDescription("企鹅物流功能指令。拉取企鹅物流信息数据。");
                break;

            case PENGUIN_QUERY_STAGE_INFO:
                document.setSubFunctionIdentifier("查作战");
                document.setSubFunctionDescription("企鹅物流功能指令。查看某个作战的理智消耗，掉落等信息。");
                document.getParameterDescriptions().add("作战名。举例：1-7。");
                break;
                
            case REMINDER_CREATE_TASK:
                document.setSubFunctionIdentifier("创建提醒");
                document.setSubFunctionDescription("事项提醒功能指令。提醒任务指的是在满足时间条件的时候发送指定消息。");
                document.getParameterDescriptions().add("时间条件-月。-1表示不限制。");
                document.getParameterDescriptions().add("时间条件-日。-1表示不限制。");
                document.getParameterDescriptions().add("时间条件-星期数。-1表示不限制。");
                document.getParameterDescriptions().add("时间条件-执行次数。-1表示不限制。");
                document.getParameterDescriptions().add("消息内容。");
                break;
            case REMINDER_LIST_TASK:
                document.setSubFunctionIdentifier("查看提醒");
                document.setSubFunctionDescription("事项提醒功能指令。查看所有已创建提醒任务。");
                break;
            case REMINDER_REMOVE_TASK:
                document.setSubFunctionIdentifier("移除提醒");
                document.setSubFunctionDescription("事项提醒功能指令。移除指定id的提醒任务。");
                document.getParameterDescriptions().add("提醒id。由查看提醒得到。");
                break;
            default:
                throw new Exception("未定义document: " + this);
        }
        return document;
    }
}
