package com.foodify.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "orderLifecycleExecutor")
    public TaskExecutor orderLifecycleExecutor(
            @Value("${async.order-lifecycle.core-pool-size:2}") int corePoolSize,
            @Value("${async.order-lifecycle.max-pool-size:4}") int maxPoolSize,
            @Value("${async.order-lifecycle.queue-capacity:100}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("order-lifecycle-");
        executor.setCorePoolSize(Math.max(1, corePoolSize));
        executor.setMaxPoolSize(Math.max(executor.getCorePoolSize(), maxPoolSize));
        executor.setQueueCapacity(Math.max(10, queueCapacity));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationDispatchExecutor")
    public TaskExecutor notificationDispatchExecutor(
            @Value("${async.notifications.core-pool-size:4}") int corePoolSize,
            @Value("${async.notifications.max-pool-size:8}") int maxPoolSize,
            @Value("${async.notifications.queue-capacity:500}") int queueCapacity
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("notification-dispatch-");
        executor.setCorePoolSize(Math.max(1, corePoolSize));
        executor.setMaxPoolSize(Math.max(executor.getCorePoolSize(), maxPoolSize));
        executor.setQueueCapacity(Math.max(50, queueCapacity));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
