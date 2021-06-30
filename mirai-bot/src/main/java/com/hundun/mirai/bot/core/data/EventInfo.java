package com.hundun.mirai.bot.core.data;

import lombok.Data;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * 包装NudgeEvent、GroupMessageEvent等所有种类的MiraiEvent
 * @author hundun
 * Created on 2021/05/07
 */
@Data
public class EventInfo {
    
    long groupId;
    //@NotNull
    long senderId;
    //@NotNull
    long targetId;
    //@NotNull
    MessageChain message;
    //@NotNull
    Bot bot;
    
}
