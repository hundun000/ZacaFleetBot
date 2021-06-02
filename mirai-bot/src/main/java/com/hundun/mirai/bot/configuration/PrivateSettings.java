package com.hundun.mirai.bot.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.Map;

import com.hundun.mirai.bot.core.GroupConfig;
import com.hundun.mirai.bot.core.UserTagConfig;

import kotlinx.serialization.Serializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Serializable
public class PrivateSettings {
    public Long adminAccount;
    public Long botAccount;
    public String botPwd;
    
    public Map<String, UserTagConfig> userTagConfigs;
    public Map<String, GroupConfig> groupConfigs;
    
}
