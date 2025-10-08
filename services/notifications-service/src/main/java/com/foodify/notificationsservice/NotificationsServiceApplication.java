package com.foodify.notificationsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.foodify.notificationsservice",
        "com.foodify.server.modules.notifications",
        "com.foodify.server.modules.identity",
        "com.foodify.server.modules.customers",
        "com.foodify.server.infrastructure.config"
})
public class NotificationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsServiceApplication.class, args);
    }
}
