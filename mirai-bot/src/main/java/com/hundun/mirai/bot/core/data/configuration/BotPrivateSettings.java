package com.hundun.mirai.bot.core.data.configuration;

import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Data
public class BotPrivateSettings {
    public Long adminAccount;
    public Long botAccount;
    public String botPwd;
    
    public Map<String, UserTagConfig> userTagConfigs;
    public Map<String, GroupConfig> groupConfigs;
}
