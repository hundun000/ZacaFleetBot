package hundun.zacafleetbot.mirai.botlogic.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.character.Amiya;
import hundun.zacafleetbot.mirai.botlogic.core.character.BaseCharacter;
import hundun.zacafleetbot.mirai.botlogic.core.character.Neko;
import hundun.zacafleetbot.mirai.botlogic.core.character.PrinzEugen;
import hundun.zacafleetbot.mirai.botlogic.core.character.ZacaMusume;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.export.IMyEventHandler;
import lombok.extern.slf4j.Slf4j;

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
