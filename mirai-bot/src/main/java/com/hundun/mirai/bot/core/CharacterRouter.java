package com.hundun.mirai.bot.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.character.Amiya;
import com.hundun.mirai.bot.core.character.BaseCharacter;
import com.hundun.mirai.bot.core.character.Neko;
import com.hundun.mirai.bot.core.character.PrinzEugen;
import com.hundun.mirai.bot.core.character.ZacaMusume;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.EventInfoFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.BotPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.GroupConfig;
import com.hundun.mirai.bot.core.data.configuration.UserTag;
import com.hundun.mirai.bot.core.data.configuration.UserTagConfig;
import com.hundun.mirai.bot.core.function.WeiboFunction;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.export.IMyEventHandler;

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
@Component
public class CharacterRouter implements IMyEventHandler {
    
    @Autowired
    Amiya amiya;
    @Autowired
    ZacaMusume zacaMusume;
    
    @Autowired
    PrinzEugen prinzEugen;
    @Autowired
    Neko neko;

    @Autowired
    SettingManager settingManager;

    
//    @Value("${account.group.arknights}")
//    public long arknightsGroupId;
//    
//    @Value("${account.group.kancolle}")
//    public long kancolleGroupId;
//    
//    @Value("${account.group.neko}")
//    public long nekoGroupId;
    
    List<BaseCharacter> characters = new ArrayList<>();
    
    
    
    @PostConstruct
    public void manualWired() {
         

        characters.add(amiya);
        characters.add(prinzEugen);
        characters.add(zacaMusume);
        characters.add(neko);
        

    }


    
    
    
    
    


    

    @Override
    public boolean onNudgeEvent(EventInfo eventInfo) throws Exception {
        synchronized (this) {
            GroupConfig config = settingManager.getGroupConfigOrEmpty(eventInfo.getBot().getId(), eventInfo.getGroupId());
            if (config == null) {
                return false;
            }
            
            
            boolean done = false;
            for (BaseCharacter character : characters) {
                if (config.getEnableCharacters().contains(character.getId())) {
                    done = character.onNudgeEvent(eventInfo);
                }   
                if (done) {
                    break;
                }
            }
            return done;
        }
    }

    @Override
    public boolean onGroupMessageEvent(EventInfo eventInfo) throws Exception {
        synchronized (this) {
            if (eventInfo.getSenderId() == eventInfo.getBot().getId()) {
                return false;
            }
            
            GroupConfig config = settingManager.getGroupConfigOrEmpty(eventInfo.getBot().getId(), eventInfo.getGroupId());
            if (config == null) {
                log.info("grop {} no groupConfig, onMessage do nothing", eventInfo.getGroupId());
                return false;
            }
            
            
            boolean done = false;
            for (BaseCharacter character : characters) {
                if (config.getEnableCharacters().contains(character.getId())) {
                    done = character.onGroupMessageEvent(eventInfo);
                }   
                if (done) {
                    break;
                }
            }
            return done;
        }
    }
}
