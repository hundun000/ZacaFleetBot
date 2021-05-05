package com.mirai.hundun.configuration;
/**
 * @author hundun
 * Created on 2021/05/07
 */

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "everyday")
@PropertySource(value = "classpath:setting.yml", factory = YamlPropertySourceFactory.class)
public class TestSettings {
    public List<String> list;
    
}
