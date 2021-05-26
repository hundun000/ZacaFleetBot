package com.hundun.mirai.plugin.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.Map;

import com.hundun.mirai.plugin.core.GroupConfig;
import com.hundun.mirai.plugin.core.UserTagConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PrivateSettings {
    public Long adminAccount;
    public Long botAccount;
    public String botPwd;
    
    public Map<String, UserTagConfig> userTagConfigs;
    public Map<String, GroupConfig> groupConfigs;
    
}
