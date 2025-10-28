package com.foodify.server.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RateLimitingFilterTest {

    private RateLimitingFilter filter;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        GuardrailProperties properties = new GuardrailProperties();
        GuardrailProperties.RateLimit rateLimit = new GuardrailProperties.RateLimit();
        rateLimit.setName("test");
        rateLimit.setCapacity(2);
        rateLimit.setWindow(Duration.ofSeconds(60));
        rateLimit.setMethod(HttpMethod.GET);
        rateLimit.setPaths(List.of(
                "/api/client/nearby/top",
                "/api/client/nearby/favorites",
                "/api/client/nearby/orders",
                "/api/client/nearby/restaurants"
        ));
        properties.setRateLimits(List.of(rateLimit));

        meterRegistry = new SimpleMeterRegistry();
        filter = new RateLimitingFilter(properties, meterRegistry);
    }

    @Test
    void allowsRequestsWithinCapacity() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/client/nearby/top");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getStatus()).isEqualTo(200);
        double allowedCount = meterRegistry.find("guardrails.rate_limit.requests")
                .tag("name", "test")
                .tag("outcome", "allowed")
                .counter()
                .count();
        assertThat(allowedCount).isEqualTo(1.0d);
    }

    @Test
    void rejectsRequestsBeyondCapacity() throws ServletException, IOException {
        filter.doFilter(new MockHttpServletRequest("GET", "/api/client/nearby/top"), new MockHttpServletResponse(), new MockFilterChain());
        filter.doFilter(new MockHttpServletRequest("GET", "/api/client/nearby/top"), new MockHttpServletResponse(), new MockFilterChain());

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        filter.doFilter(new MockHttpServletRequest("GET", "/api/client/nearby/top"), blockedResponse, new MockFilterChain());

        assertThat(blockedResponse.getStatus()).isEqualTo(429);
        assertThat(blockedResponse.getHeader("Retry-After")).isNotBlank();

        double rejectedCount = meterRegistry.find("guardrails.rate_limit.requests")
                .tag("name", "test")
                .tag("outcome", "rejected")
                .counter()
                .count();
        assertThat(rejectedCount).isEqualTo(1.0d);
    }
}
