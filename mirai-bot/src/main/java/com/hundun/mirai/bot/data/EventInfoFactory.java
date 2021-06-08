package com.hundun.mirai.bot.data;


import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/05/07
 */
public class EventInfoFactory {
    public static EventInfo get(GroupMessageEvent event, String characterId) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = event.getGroup().getId();
        eventInfo.senderId = event.getSender().getId();
        eventInfo.targetId = -1;
        eventInfo.message = event.getMessage();
        eventInfo.characterId = characterId;
        eventInfo.bot = event.getBot();
        return eventInfo;
    }
    public static EventInfo get(NudgeEvent event, String characterId) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = event.getSubject().getId();
        eventInfo.senderId = event.getFrom().getId();
        eventInfo.targetId = event.getTarget().getId();
        eventInfo.message = null;
        eventInfo.characterId = characterId;
        eventInfo.bot = event.getBot();
        return eventInfo;
    }
    
    public static EventInfo get(
            long groupId,
            long senderId,
            long targetId,
            String message, 
            String characterId
            ) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = groupId;
        eventInfo.senderId = senderId;
        eventInfo.targetId = targetId;
        eventInfo.characterId = characterId;
        eventInfo.message = MessageUtils.newChain(new PlainText(message));
        eventInfo.bot = null;
        return eventInfo;
    }
}
