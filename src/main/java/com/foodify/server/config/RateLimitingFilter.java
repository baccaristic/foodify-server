package com.foodify.server.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RateLimitingFilter extends OncePerRequestFilter {

    private final List<Rule> rules;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public RateLimitingFilter(GuardrailProperties properties, MeterRegistry registry) {
        this.rules = buildRules(properties, registry);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Rule matchedRule = findRule(request);
        if (matchedRule == null) {
            filterChain.doFilter(request, response);
            return;
        }

        long now = System.nanoTime();
        if (matchedRule.rateLimiter.tryAcquire(now)) {
            matchedRule.allowedCounter.increment();
            filterChain.doFilter(request, response);
            return;
        }

        matchedRule.rejectedCounter.increment();
        response.setStatus(matchedRule.httpStatus);
        long nanosToReset = matchedRule.rateLimiter.nanosUntilReset(now);
        if (nanosToReset > 0) {
            long retryAfterSeconds = Duration.ofNanos(nanosToReset).toSeconds();
            response.setHeader("Retry-After", Long.toString(Math.max(1, retryAfterSeconds)));
        }
    }

    private Rule findRule(HttpServletRequest request) {
        String path = request.getRequestURI();
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        for (Rule rule : rules) {
            if (rule.matches(path, method)) {
                return rule;
            }
        }
        return null;
    }

    private List<Rule> buildRules(GuardrailProperties properties, MeterRegistry registry) {
        List<Rule> builtRules = new ArrayList<>();
        if (properties == null || properties.getRateLimits() == null) {
            return builtRules;
        }

        for (GuardrailProperties.RateLimit rateLimit : properties.getRateLimits()) {
            if (rateLimit.getPaths().isEmpty() || rateLimit.getCapacity() <= 0) {
                continue;
            }
            FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(rateLimit.getCapacity(), rateLimit.getWindow());
            Counter allowedCounter = Counter.builder("guardrails.rate_limit.requests")
                    .tag("name", safeTag(rateLimit.getName()))
                    .tag("outcome", "allowed")
                    .register(registry);
            Counter rejectedCounter = Counter.builder("guardrails.rate_limit.requests")
                    .tag("name", safeTag(rateLimit.getName()))
                    .tag("outcome", "rejected")
                    .register(registry);
            builtRules.add(new Rule(rateLimit, limiter, allowedCounter, rejectedCounter));
        }
        return builtRules;
    }

    private String safeTag(String value) {
        if (value == null || value.isBlank()) {
            return "unnamed";
        }
        return value.toLowerCase(Locale.ROOT).replace(' ', '-');
    }

    private final class Rule {
        private final GuardrailProperties.RateLimit rateLimit;
        private final FixedWindowRateLimiter rateLimiter;
        private final Counter allowedCounter;
        private final Counter rejectedCounter;
        private final int httpStatus;

        private Rule(GuardrailProperties.RateLimit rateLimit,
                     FixedWindowRateLimiter rateLimiter,
                     Counter allowedCounter,
                     Counter rejectedCounter) {
            this.rateLimit = rateLimit;
            this.rateLimiter = rateLimiter;
            this.allowedCounter = allowedCounter;
            this.rejectedCounter = rejectedCounter;
            this.httpStatus = rateLimit.getHttpStatus();
        }

        private boolean matches(String path, HttpMethod method) {
            if (path == null) {
                return false;
            }
            HttpMethod expected = rateLimit.getMethod();
            if (expected != null && method != null && !Objects.equals(expected, method)) {
                return false;
            }
            for (String pattern : rateLimit.getPaths()) {
                if (pathMatcher.match(pattern, path)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class FixedWindowRateLimiter {
        private final int capacity;
        private final long windowNanos;
        private int count;
        private long windowStartNanos;

        private FixedWindowRateLimiter(int capacity, Duration window) {
            this.capacity = Math.max(1, capacity);
            Duration effectiveWindow = window != null && !window.isNegative() && !window.isZero()
                    ? window
                    : Duration.ofSeconds(1);
            this.windowNanos = effectiveWindow.toNanos();
            this.windowStartNanos = System.nanoTime();
            this.count = 0;
        }

        private synchronized boolean tryAcquire(long now) {
            refreshWindowIfNeeded(now);
            if (count < capacity) {
                count++;
                return true;
            }
            return false;
        }

        private synchronized long nanosUntilReset(long now) {
            refreshWindowIfNeeded(now);
            long elapsed = now - windowStartNanos;
            long remaining = windowNanos - elapsed;
            return Math.max(remaining, 0L);
        }

        private void refreshWindowIfNeeded(long now) {
            if (now - windowStartNanos >= windowNanos) {
                windowStartNanos = now;
                count = 0;
            }
        }
    }
}
