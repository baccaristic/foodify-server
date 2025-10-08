package com.foodify.ordersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.foodify.ordersservice",
        "com.foodify.server.modules.orders",
        "com.foodify.server.modules.addresses",
        "com.foodify.server.modules.identity",
        "com.foodify.server.modules.customers",
        "com.foodify.server.modules.notifications",
        "com.foodify.server.modules.delivery",
        "com.foodify.server.infrastructure.config"
})
@EnableKafka
@EnableScheduling
public class OrdersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersServiceApplication.class, args);
    }
}
