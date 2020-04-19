package com.sse.demo.config;

import com.sse.demo.services.FluxNotificationProcessor;
import com.sse.demo.models.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.DirectProcessor;

@Configuration
public class FluxProcessorsConfig {

    @Bean
    public FluxNotificationProcessor notificationsProcessor() {
        return new FluxNotificationProcessor(
            DirectProcessor.<Notification>create().serialize()
        );
    }
}
