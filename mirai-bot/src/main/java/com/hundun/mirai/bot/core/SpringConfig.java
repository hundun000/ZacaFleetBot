package com.hundun.mirai.bot.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author hundun
 * Created on 2021/06/30
 */
@Configuration
@ComponentScan(basePackages={"com.hundun.mirai.bot"})
@EnableMongoRepositories(basePackages = {"com.hundun.mirai.bot"})
public class SpringConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "mirai";
    }


    
}
