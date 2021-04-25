package com.mirai.hundun.bot;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;


/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IAtHandler {
    boolean acceptAt(GroupMessageEvent event, At plainText);
}
