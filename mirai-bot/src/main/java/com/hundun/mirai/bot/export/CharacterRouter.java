package com.hundun.mirai.bot.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.IManualWired;
import com.hundun.mirai.bot.character.Amiya;
import com.hundun.mirai.bot.character.BaseCharacter;
import com.hundun.mirai.bot.character.Neko;
import com.hundun.mirai.bot.character.PrinzEugen;
import com.hundun.mirai.bot.character.ZacaMusume;
import com.hundun.mirai.bot.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.data.BotPrivateSettings;
import com.hundun.mirai.bot.data.EventInfo;
import com.hundun.mirai.bot.data.EventInfoFactory;
import com.hundun.mirai.bot.data.GroupConfig;
import com.hundun.mirai.bot.data.UserTag;
import com.hundun.mirai.bot.data.UserTagConfig;
import com.hundun.mirai.bot.function.WeiboFunction;

import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
public class CharacterRouter implements IManualWired {

    IConsole console;
    
    Amiya amiya;
    
    ZacaMusume zacaMusume;
    
    PrinzEugen prinzEugen;
    
    Neko neko;
    
    WeiboFunction weiboFunction;

    Map<Long, Map<Long, GroupConfig>> botIdToGroupConfigs = new HashMap<>();

    Map<Long, Map<Long, UserTagConfig>> botIdToUserTagConfigs = new HashMap<>();
    
//    @Value("${account.group.arknights}")
//    public long arknightsGroupId;
//    
//    @Value("${account.group.kancolle}")
//    public long kancolleGroupId;
//    
//    @Value("${account.group.neko}")
//    public long nekoGroupId;
    
    List<BaseCharacter> characters = new ArrayList<>();
    
    
    AppPrivateSettings appPrivateSettings;
    @Override
    public void manualWired() {
        this.console = CustomBeanFactory.getInstance().console;
        this.amiya = CustomBeanFactory.getInstance().amiya;
        this.zacaMusume = CustomBeanFactory.getInstance().zacaMusume;
        this.prinzEugen = CustomBeanFactory.getInstance().prinzEugen;
        this.neko = CustomBeanFactory.getInstance().neko;
        this.weiboFunction = CustomBeanFactory.getInstance().weiboFunction;
        this.appPrivateSettings = CustomBeanFactory.getInstance().appPrivateSettings;

    }
    
    private void readConfigFile() {
        for (BotPrivateSettings botPrivateSettings : appPrivateSettings.getBotPrivateSettingsMap().values()) {
            
            Map<Long, GroupConfig> groupConfigs = new HashMap<>();
            for (Entry<String, GroupConfig> entry : botPrivateSettings.getGroupConfigs().entrySet()) {
                GroupConfig config = entry.getValue();
                config.setGroupDescription(entry.getKey());
                
                groupConfigs.put(config.getGroupId(), config);
                
            }
            botIdToGroupConfigs.put(botPrivateSettings.getBotAccount(), groupConfigs);
            
            Map<Long, UserTagConfig> userTagConfigs = new HashMap<>();
            for (Entry<String, UserTagConfig> entry : botPrivateSettings.getUserTagConfigs().entrySet()) {
                UserTagConfig config = entry.getValue();
                
                userTagConfigs.put(config.getId(), config);
                
            }
            botIdToUserTagConfigs.put(botPrivateSettings.getBotAccount(), userTagConfigs);
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
    
    
    @NotNull
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception {
        
        synchronized (this) {
            GroupConfig config = botIdToGroupConfigs.get(event.getBot().getId()).get(event.getSubject().getId());
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
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理

        synchronized (this) {
            if (event.getSender().getId() == event.getBot().getId()) {
                return ListeningStatus.LISTENING;
            }
            
            Map<Long, GroupConfig> groupConfigs = botIdToGroupConfigs.get(event.getBot().getId());
            if (groupConfigs == null) {
                log.info("bot {} no groupConfigs, onMessage do nothing", event.getBot().getId());
                return ListeningStatus.LISTENING;
            }
            GroupConfig config = groupConfigs.get(event.getGroup().getId());
            if (config == null) {
                log.info("grop {} no groupConfig in {}, onMessage do nothing", event.getGroup().getId(), groupConfigs);
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


    public Collection<GroupConfig> getGroupConfigsOrEmpty(long botAccountId) {
        if (!botIdToGroupConfigs.containsKey(botAccountId)) {
            return new ArrayList<>(0);
        }
        return botIdToGroupConfigs.get(botAccountId).values();
    }

    public List<UserTag> getUserTagsOrEmpty(long botId, Long userId) {
        if (!botIdToUserTagConfigs.containsKey(botId)) {
            return new ArrayList<>(0);
        }
        Map<Long, UserTagConfig> userTagConfigs = botIdToUserTagConfigs.get(botId);
        if (!userTagConfigs.containsKey(userId)) {
            return new ArrayList<>(0);
        }
        return userTagConfigs.get(userId).getTags();
    }

    public List<String> getGroupCharacterIdsOrEmpty(long botId, Long groupId) {
        if (!botIdToGroupConfigs.containsKey(botId)) {
            return new ArrayList<>();
        }
        Map<Long, GroupConfig> groupConfigs = botIdToGroupConfigs.get(botId);
        if (!groupConfigs.containsKey(groupId)) {
            return new ArrayList<>();
        }
        return groupConfigs.get(groupId).getEnableCharacters();
    }

    public long getAdminAccount(long botId) {
        for (BotPrivateSettings botPrivateSettings : appPrivateSettings.getBotPrivateSettingsMap().values()) {
            if (botPrivateSettings.getBotAccount() == botId) {
                return botPrivateSettings.getAdminAccount();
            }
        }
        
        return -1;
    }
}
