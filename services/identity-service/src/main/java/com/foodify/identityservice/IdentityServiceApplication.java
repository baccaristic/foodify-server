package com.foodify.identityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.foodify.identityservice",
        "com.foodify.server.modules.identity",
        "com.foodify.server.modules.auth",
        "com.foodify.server.modules.customers",
        "com.foodify.server.infrastructure.config"
})
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
