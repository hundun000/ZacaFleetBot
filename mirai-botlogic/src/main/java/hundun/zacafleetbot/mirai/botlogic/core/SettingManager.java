package hundun.zacafleetbot.mirai.botlogic.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPrivateSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.AppPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.BotPrivateSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.CharacterPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.UserTag;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.UserTagConfig;

/**
 * 由于需要通过插件读取配置，所以不是通过spring常用的@Condifuration
 * @author hundun
 * Created on 2021/06/25
 */
@Component
public class SettingManager {

    Map<Long, Map<Long, GroupConfig>> botIdToGroupConfigs = new HashMap<>();

    Map<Long, Map<Long, UserTagConfig>> botIdToUserTagConfigs = new HashMap<>();
    
    @Autowired
    private AppPrivateSettings appPrivateSettings;
    
    @Autowired
    private AppPublicSettings appPublicSettings;
    
    @PostConstruct
    public void postConsoleBind() {
        //this.appPrivateSettings = CustomBeanFactory.getInstance().appPrivateSettings;
        
        for (BotPrivateSettings botPrivateSettings : appPrivateSettings.getBotPrivateSettingsList()) {
            
            Map<Long, GroupConfig> groupConfigs = new HashMap<>();
            for (GroupConfig config : botPrivateSettings.getGroupConfigs()) {
                
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
        for (BotPrivateSettings botPrivateSettings : appPrivateSettings.getBotPrivateSettingsList()) {
            if (botPrivateSettings.getBotAccount() == botId) {
                return botPrivateSettings.getAdminAccount();
            }
        }
        
        return -1;
    }

    public GroupConfig getGroupConfigOrEmpty(long botId, long groupId) {
        if (!botIdToGroupConfigs.containsKey(botId)) {
            return null;
        }
        return botIdToGroupConfigs.get(botId).get(groupId);
    }
    
    public CharacterPublicSettings getCharacterPublicSettings(String characterId) {
        return appPublicSettings.getCharacterIdToPublicSettings().get(characterId);
    }
    
}
