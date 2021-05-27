package com.hundun.mirai.plugin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.IManualWired;
import com.hundun.mirai.plugin.character.Amiya;
import com.hundun.mirai.plugin.character.BaseCharacter;
import com.hundun.mirai.plugin.character.Neko;
import com.hundun.mirai.plugin.character.PrinzEugen;
import com.hundun.mirai.plugin.character.ZacaMusume;
import com.hundun.mirai.plugin.configuration.PrivateSettings;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.EventInfoFactory;
import com.hundun.mirai.plugin.core.GroupConfig;
import com.hundun.mirai.plugin.core.UserTag;
import com.hundun.mirai.plugin.core.UserTagConfig;
import com.hundun.mirai.plugin.function.WeiboFunction;

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
public class CharacterRouter extends SimpleListenerHost implements IManualWired {

    BotService botService;
    
    Amiya amiya;
    
    ZacaMusume zacaMusume;
    
    PrinzEugen prinzEugen;
    
    Neko neko;
    
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
    
    
    PrivateSettings privateSettings;
    @Override
    public void manualWired() {
        this.botService = CustomBeanFactory.getInstance().botService;
        this.amiya = CustomBeanFactory.getInstance().amiya;
        this.zacaMusume = CustomBeanFactory.getInstance().zacaMusume;
        this.prinzEugen = CustomBeanFactory.getInstance().prinzEugen;
        this.neko = CustomBeanFactory.getInstance().neko;
        this.weiboFunction = CustomBeanFactory.getInstance().weiboFunction;
        this.privateSettings = CustomBeanFactory.getInstance().privateSettings;


    }
    
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

    
    @Override
    public void afterManualWired() {

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
