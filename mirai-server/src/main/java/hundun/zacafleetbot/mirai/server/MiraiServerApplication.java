package hundun.zacafleetbot.mirai.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication(scanBasePackages = {"hundun.zacafleetbot.mirai.server"})
@EnableConfigurationProperties
public class MiraiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiraiServerApplication.class, args);
	}
	
}
