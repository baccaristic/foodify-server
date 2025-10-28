package com.foodify.server.modules.payments.konnect;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KonnectConfiguration {

    @Bean
    public RestTemplate konnectRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
