package com.mirai.hundun.bot;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
public interface IPlainTextHandler {
    boolean acceptPlainText(GroupMessageEvent event, PlainText plainText);
}
