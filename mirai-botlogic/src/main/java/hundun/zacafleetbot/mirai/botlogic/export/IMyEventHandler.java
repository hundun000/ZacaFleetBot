package hundun.zacafleetbot.mirai.botlogic.export;

import org.jetbrains.annotations.NotNull;

import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;

/**
 * @author hundun
 * Created on 2021/06/16
 */
public interface IMyEventHandler {
    boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception;
    boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception;

}
