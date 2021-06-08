package com.hundun.mirai.bot.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.List;
import java.util.Map;

import com.hundun.mirai.bot.data.BotPrivateSettings;
import com.hundun.mirai.bot.data.GroupConfig;
import com.hundun.mirai.bot.data.UserTagConfig;

import kotlinx.serialization.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Serializable
public class AppPrivateSettings {
    
    Map<String ,BotPrivateSettings> botPrivateSettingsMap;
}
