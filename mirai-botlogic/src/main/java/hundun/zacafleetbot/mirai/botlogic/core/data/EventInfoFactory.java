package hundun.zacafleetbot.mirai.botlogic.core.data;


import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/05/07
 */
public class EventInfoFactory {
    public static EventInfo get(GroupMessageEvent event) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = event.getGroup().getId();
        eventInfo.senderId = event.getSender().getId();
        eventInfo.targetId = -1;
        eventInfo.message = event.getMessage();
        eventInfo.miraiEventClass = GroupMessageEvent.class;
        eventInfo.bot = event.getBot();
        return eventInfo;
    }
    public static EventInfo get(NudgeEvent event) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = event.getSubject().getId();
        eventInfo.senderId = event.getFrom().getId();
        eventInfo.targetId = event.getTarget().getId();
        eventInfo.message = null;
        eventInfo.miraiEventClass = NudgeEvent.class;
        eventInfo.bot = event.getBot();
        return eventInfo;
    }
    
    public static EventInfo fakeGroupMessageEvent(
            long groupId,
            long senderId,
            long targetId,
            String message
            ) {
        EventInfo eventInfo = new EventInfo();
        eventInfo.groupId = groupId;
        eventInfo.senderId = senderId;
        eventInfo.targetId = targetId;
        eventInfo.message = MessageUtils.newChain(new PlainText(message));
        eventInfo.miraiEventClass = GroupMessageEvent.class;
        eventInfo.bot = null;
        return eventInfo;
    }
}
