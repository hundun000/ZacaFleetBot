package com.hundun.mirai.bot.export;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.data.EventInfo;

import net.mamoe.mirai.event.ListeningStatus;

/**
 * @author hundun
 * Created on 2021/06/16
 */
public interface IMyEventHandler {
    boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception;
    boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception;

}
