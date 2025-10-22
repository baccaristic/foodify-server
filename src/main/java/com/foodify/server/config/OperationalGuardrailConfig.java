package com.foodify.server.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ExecutorServiceMetrics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class OperationalGuardrailConfig {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> guardrailRateLimitingFilter(
            GuardrailProperties guardrailProperties,
            MeterRegistry meterRegistry
    ) {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter(guardrailProperties, meterRegistry));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registrationBean;
    }

    @Bean
    public MeterBinder asyncExecutorMetrics(
            @Qualifier("orderLifecycleExecutor") TaskExecutor orderLifecycleExecutor,
            @Qualifier("notificationDispatchExecutor") TaskExecutor notificationDispatchExecutor
    ) {
        return registry -> {
            bindThreadPoolExecutor("order-lifecycle", orderLifecycleExecutor, registry);
            bindThreadPoolExecutor("notification-dispatch", notificationDispatchExecutor, registry);
        };
    }

    private void bindThreadPoolExecutor(String name, TaskExecutor executor, MeterRegistry registry) {
        if (executor instanceof ThreadPoolTaskExecutor threadPoolTaskExecutor) {
            if (threadPoolTaskExecutor.getThreadPoolExecutor() != null) {
                ExecutorServiceMetrics.monitor(
                        registry,
                        threadPoolTaskExecutor.getThreadPoolExecutor(),
                        "async.executor",
                        Tags.of("executor", name)
                );
                registry.gauge(
                        "async.executor.queue.size",
                        Tags.of("executor", name),
                        threadPoolTaskExecutor,
                        source -> {
                            if (source.getThreadPoolExecutor() == null || source.getThreadPoolExecutor().getQueue() == null) {
                                return 0;
                            }
                            return source.getThreadPoolExecutor().getQueue().size();
                        }
                );
            }
        }
    }
}
