package com.hundun.mirai.bot.helper;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.AppPublicSettings;

/**
 * @author hundun
 * Created on 2021/06/21
 */
public class Utils {
    public static String checkFolder(String subFolerName, String parentFoler) {
        File directory = new File(parentFoler);
        if (! directory.exists()){
            directory.mkdir();
        }
        
        File subFoler = new File(parentFoler + subFolerName);
        if (! subFoler.exists()){
            subFoler.mkdir();
        }
        
        return parentFoler + subFolerName + File.separator;
    }
    
    static ObjectMapper objectMapper = new ObjectMapper();
    
    public static AppPrivateSettings parseAppPrivateSettings(File settingsFile) throws Exception {
        AppPrivateSettings appPrivateSettings;
        
        appPrivateSettings = objectMapper.readValue(settingsFile, AppPrivateSettings.class);

        return appPrivateSettings;
    }

    public static AppPublicSettings parseAppPublicSettings(File settingsFile) throws Exception {
        AppPublicSettings appPublicSettings;
        appPublicSettings = objectMapper.readValue(settingsFile, AppPublicSettings.class);
        return appPublicSettings;
    }
    
}
