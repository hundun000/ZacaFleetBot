package com.hundun.mirai.bot.service;

import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/06/03
 */
public interface IConsole {
    
    
    
    public void sendToGroup(Long groupId, String message);
    public void sendToGroup(long groupId, MessageChain messageChain);
    
    public Image uploadImage(Long groupId, ExternalResource externalResource);
    public Voice uploadVoice(Long groupId, ExternalResource externalResource);
    
    public long getSelfAccount();
    public Long getAdminAccount();
}
