package hundun.zacafleetbot.mirai.botlogic.core;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.PreHandleablePrioritySelectorBTNode;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.character.Amiya;
import hundun.zacafleetbot.mirai.botlogic.core.character.BaseCharacter;
import hundun.zacafleetbot.mirai.botlogic.core.character.Neko;
import hundun.zacafleetbot.mirai.botlogic.core.character.PrinzEugen;
import hundun.zacafleetbot.mirai.botlogic.core.character.ZacaMusume;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class CharacterRouter extends PreHandleablePrioritySelectorBTNode {
    
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
    
    @Autowired
    IConsole console;

    
    @PostConstruct
    public void manualWired() {
         

        addChild(amiya);
        addChild(prinzEugen);
        addChild(zacaMusume);
        addChild(neko);
        
        
    }


    
    @Override
    public boolean selfEnable(BlackBoard blackBoard) {

        EventInfo eventInfo = blackBoard.getEvent();
        
        GroupConfig config = settingManager.getGroupConfigOrEmpty(eventInfo.getBot().getId(), eventInfo.getGroupId());
        if (config == null) {
            console.getLogger().debug("grop " + eventInfo.getGroupId() +" no groupConfig, onMessage do nothing");
            return false;
        }
        
        return true;
    }
    


    

    

    
}
