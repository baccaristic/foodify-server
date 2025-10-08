package com.foodify.server.infrastructure.config;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.ObservationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    @Bean
    public ObservationFilter serviceNameObservationFilter(
            @Value("${spring.application.name:foodify-platform}") String applicationName) {
        return context -> {
            context.addLowCardinalityKeyValue(KeyValue.of("service.name", applicationName));
            return context;
        };
    }
}
