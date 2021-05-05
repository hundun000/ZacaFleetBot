package com.mirai.hundun.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.character.BaseCharacter;
import com.mirai.hundun.character.PrinzEugen;
import com.mirai.hundun.character.ZacaMusume;
import com.mirai.hundun.character.function.WeiboFunction;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.EventInfoFactory;
import com.mirai.hundun.core.GroupConfig;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.parser.statement.Statement;

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
    
    List<BaseCharacter> characters = new ArrayList<>();
    
    
    //Map<Long, List<String>> groupIdToCharacterIds = new HashMap<>();
    
    
    /**
     * TODO read from config file
     */
    private void fakeReadConfigFile() {
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
        groupConfigs.put(config.getGroupId(), config);
    }
    
    
    @PostConstruct
    public void init() {
        characters.add(amiya);
        characters.add(prinzEugen);
        characters.add(zacaMusume);
        
        
        fakeReadConfigFile();

        log.info("groupConfigs = {}", groupConfigs);
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
            for (BaseCharacter character : characters) {
                EventInfo eventInfo = EventInfoFactory.get(event, character.getId());
                if (config.getEnableCharacters().contains(character.getId())) {
                    done = character.onNudgeEvent(eventInfo);
                }   
                if (done) {
                    break;
                }
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
            for (BaseCharacter character : characters) {
                EventInfo eventInfo = EventInfoFactory.get(event, character.getId());
                if (config.getEnableCharacters().contains(character.getId())) {
                    done = character.onGroupMessageEvent(eventInfo);
                }   
                if (done) {
                    break;
                }
            }
        }
        
        return ListeningStatus.LISTENING;
    }


    public Map<Long, GroupConfig> getGroupConfigs() {
        return groupConfigs;
    }


    public List<String> getGroupCharacterIds(Long groupId) {
        if (!groupConfigs.containsKey(groupId)) {
            return new ArrayList<>();
        }
        return groupConfigs.get(groupId).getEnableCharacters();
    }
}
