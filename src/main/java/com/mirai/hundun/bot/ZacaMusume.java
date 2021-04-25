package com.mirai.hundun.bot;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class ZacaMusume implements Consumer<GroupMessageEvent> {

    @Value("${account.group.arknights}")
    public long arknightsGroupId;
    
    @Autowired
    BotService botService;
    
    @Override
    public void accept(GroupMessageEvent t) {
        // TODO Auto-generated method stub
        
    }

}
