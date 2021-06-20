package com.hundun.mirai.bot.helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;

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

    public static PublicSettings parseAppPublicSettings(File settingsFile) throws Exception {
        PublicSettings publicSettings;
        publicSettings = objectMapper.readValue(settingsFile, PublicSettings.class);
        return publicSettings;
    }
    
}
