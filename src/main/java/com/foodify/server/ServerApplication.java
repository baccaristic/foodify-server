package com.foodify.server;

import com.foodify.server.modules.restaurants.sync.CatalogCdcProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
@EnableConfigurationProperties(CatalogCdcProperties.class)
public class ServerApplication {

        public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
