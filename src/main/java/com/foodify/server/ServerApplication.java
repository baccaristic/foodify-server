package com.foodify.server;

import com.foodify.server.config.GuardrailProperties;
import com.foodify.server.config.OrderViewProperties;
import com.foodify.server.modules.delivery.application.DriverSessionSettings;
import com.foodify.server.modules.delivery.config.DriverAssignmentProperties;
import com.foodify.server.modules.payments.konnect.KonnectProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
@EnableConfigurationProperties({DriverAssignmentProperties.class, DriverSessionSettings.class, GuardrailProperties.class, OrderViewProperties.class, KonnectProperties.class})
public class ServerApplication {

        public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
