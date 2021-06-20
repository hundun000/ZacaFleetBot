package com.hundun.mirai.bot.core.data.configuration;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/28
 */
@Data
public class PublicSettings {
    //public String[] amiyaListenWeiboUids = new String[0];
    //public String[] prinzEugenListenWeiboUids = new String[0];
    //public String[] zacaMusumeListenWeiboUids = new String[0];
    Map<String, String[]> characterListenWeiboUids;
    
    
    public String[] valueOrDefault(String characterId) {
        if (characterListenWeiboUids == null || !characterListenWeiboUids.containsKey(characterId)) {
            return new String[0];
        } else {
            return characterListenWeiboUids.get(characterId);
        }
    }
}
