package com.foodify.deliveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.foodify.deliveryservice",
        "com.foodify.server.modules.delivery",
        "com.foodify.server.modules.identity",
        "com.foodify.server.modules.customers",
        "com.foodify.server.infrastructure.config"
})
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }
}
