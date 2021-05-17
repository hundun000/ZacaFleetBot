package com.mirai.hundun.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.character.BaseCharacter;
import com.mirai.hundun.character.Neko;
import com.mirai.hundun.character.PrinzEugen;
import com.mirai.hundun.character.ZacaMusume;
import com.mirai.hundun.configuration.PrivateSettings;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.EventInfoFactory;
import com.mirai.hundun.core.GroupConfig;
import com.mirai.hundun.core.UserTag;
import com.mirai.hundun.core.UserTagConfig;
import com.mirai.hundun.function.WeiboFunction;
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
    Neko neko;
    
    @Autowired
    WeiboFunction weiboFunction;

    Map<Long, GroupConfig> groupConfigs = new HashMap<>();

    Map<Long, UserTagConfig> userTagConfigs = new HashMap<>();
    
//    @Value("${account.group.arknights}")
//    public long arknightsGroupId;
//    
//    @Value("${account.group.kancolle}")
//    public long kancolleGroupId;
//    
//    @Value("${account.group.neko}")
//    public long nekoGroupId;
    
    List<BaseCharacter> characters = new ArrayList<>();
    
    
    @Autowired
    PrivateSettings privateSettings;
    

    private void readConfigFile() {
        for (Entry<String, GroupConfig> entry : privateSettings.getGroupConfigs().entrySet()) {
            GroupConfig config = entry.getValue();
            config.setGroupDescription(entry.getKey());
            groupConfigs.put(config.getGroupId(), config);
        }
        
        for (Entry<String, UserTagConfig> entry : privateSettings.getUserTagConfigs().entrySet()) {
            UserTagConfig config = entry.getValue();
            userTagConfigs.put(config.getId(), config);
        }
    }
    
    
    @PostConstruct
    public void init() {
        characters.add(amiya);
        characters.add(prinzEugen);
        characters.add(zacaMusume);
        characters.add(neko);
        
        readConfigFile();

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

    public List<UserTag> getUserTags(Long userId) {
        if (!userTagConfigs.containsKey(userId)) {
            return new ArrayList<>();
        }
        return userTagConfigs.get(userId).getTags();
    }

    public List<String> getGroupCharacterIds(Long groupId) {
        if (!groupConfigs.containsKey(groupId)) {
            return new ArrayList<>();
        }
        return groupConfigs.get(groupId).getEnableCharacters();
    }
}
