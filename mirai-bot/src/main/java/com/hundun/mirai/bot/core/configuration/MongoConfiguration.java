package com.hundun.mirai.bot.core.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;



/**
 * @author hundun
 * Created on 2021/07/01
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.hundun.mirai.bot")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "mirai";
    }
    
    
}
