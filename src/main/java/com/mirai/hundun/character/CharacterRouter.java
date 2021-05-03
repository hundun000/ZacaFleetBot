package com.mirai.hundun.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.mirai.hundun.character.function.WeiboFunction;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class CharacterRouter extends SimpleListenerHost {

    @Autowired
    BotService botService;
    
    @Autowired
    Amiya amiya;
    
    @Autowired
    ZacaMusume zacaMusume;
    
    @Autowired
    PrinzEugen prinzEugen;
    
    @Autowired
    WeiboFunction weiboFunction;

    Map<Long, GroupConfig> groupConfigs = new HashMap<>();

    @Value("${account.group.arknights}")
    public long arknightsGroupId;
    
    @Value("${account.group.kancolle}")
    public long kancolleGroupId;
    
    @PostConstruct
    public void init() {
        
        GroupConfig config;
        config = new GroupConfig();
        config.setGroupDescription("kancolleGroup");
        config.setGroupId(kancolleGroupId);
        config.setEnableCharacters(Arrays.asList(prinzEugen.getId(), zacaMusume.getId()));
        groupConfigs.put(config.getGroupId(), config);
        config = new GroupConfig();
        config.setGroupDescription("arknightsGroup");
        config.setGroupId(arknightsGroupId);
        config.setEnableCharacters(Arrays.asList(amiya.getId(), zacaMusume.getId()));
        //config.setObserveBlogUids(Arrays.asList(WeiboService.yjUid, WeiboService.CHOSSHANLAND_UID));
        groupConfigs.put(config.getGroupId(), config);
        
        
        for (GroupConfig groupConfig : groupConfigs.values()) {
            if (groupConfig.getEnableCharacters().contains(amiya.getId())) {
                weiboFunction.putGroupIdToCharacter(groupConfig.getGroupId(), amiya.getId());
            }
        }

    }
    
    
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        // 处理事件处理时抛出的异常
        log.warn("事件处理时异常:", exception);
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception {
        
        synchronized (this) {
            GroupConfig config = groupConfigs.get(event.getSubject().getId());
            if (config == null) {
                return ListeningStatus.LISTENING;
            }
            
            boolean done = false;
            if (!done && config.getEnableCharacters().contains(amiya.getId())) {
                done = amiya.onNudgeEventMessage(event);
            }
        }
        
        return ListeningStatus.LISTENING; 
    }

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理

        synchronized (this) {
            if (event.getSender().getId() == botService.getSelfAccount()) {
                return ListeningStatus.LISTENING;
            }
            
            GroupConfig config = groupConfigs.get(event.getGroup().getId());
            if (config == null) {
                return ListeningStatus.LISTENING;
            }
            
            boolean done = false;
            if (!done && config.getEnableCharacters().contains(amiya.getId())) {
                done = amiya.onGroupMessageEventMessage(event);
            }   
            if (!done && config.getEnableCharacters().contains(prinzEugen.getId())) {
                done = prinzEugen.onGroupMessageEventMessage(event);
            }
            if (!done && config.getEnableCharacters().contains(zacaMusume.getId())) {
                done = zacaMusume.onGroupMessageEventMessage(event);
            }
        }
        
        return ListeningStatus.LISTENING;
    }
}
