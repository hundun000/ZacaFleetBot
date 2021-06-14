package com.hundun.mirai.server.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "")
@PropertySource(value = "classpath:private-settings.yml", factory = YamlPropertySourceFactory.class)
public class PrivateSettingsLoader {
    public AppPrivateSettings appPrivateSettings;

}
