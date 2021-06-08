package com.hundun.mirai.bot.export;

import java.util.Collection;
import java.util.List;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/06/03
 */
public interface IConsole {
    
    
    
    default void sendToGroup(Bot bot, long groupId, String message) {
        sendToGroup(bot, groupId, new MessageChainBuilder().append(message).asMessageChain());
    }
    public void sendToGroup(Bot bot, long groupId, MessageChain messageChain);
    
    public Image uploadImage(Bot bot, long groupId, ExternalResource externalResource);
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource);
    
    
    Collection<Bot> getBots();
    Bot getBot(long botId);
}
