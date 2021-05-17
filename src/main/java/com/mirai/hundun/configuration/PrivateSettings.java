package com.mirai.hundun.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.mirai.hundun.core.GroupConfig;
import com.mirai.hundun.core.UserTagConfig;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "")
@PropertySource(value = "classpath:private-settings.yml", factory = YamlPropertySourceFactory.class)
public class PrivateSettings {
    public Long adminAccount;
    public Long botAccount;
    public String botPwd;
    
    public Map<String, UserTagConfig> userTagConfigs;
    public Map<String, GroupConfig> groupConfigs;
    
    
    @PostConstruct
    public void show() {
        log.info("PrivateSettings = {}", this);
    }
}
