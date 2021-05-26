package com.hundun.mirai.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.server.configuration.PrivateSettingsLoader;


@EnableScheduling
@EnableFeignClients(basePackages = {"com.hundun.mirai", "com.zaca.stillstanding"})
@SpringBootApplication(scanBasePackages = {"com.hundun.mirai", "com.zaca.stillstanding"})
@EnableConfigurationProperties
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@PostConstruct
	public void p() {
	    
	}
}
