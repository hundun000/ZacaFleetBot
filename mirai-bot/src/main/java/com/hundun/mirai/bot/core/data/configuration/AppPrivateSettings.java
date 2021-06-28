package com.hundun.mirai.bot.core.data.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlinx.serialization.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Serializable
public class AppPrivateSettings {
    
    List<BotPrivateSettings> botPrivateSettingsList = new ArrayList<>(0);
}
