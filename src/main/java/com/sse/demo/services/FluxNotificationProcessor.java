package com.sse.demo.services;

import com.sse.demo.models.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

public class FluxNotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FluxNotificationProcessor.class);

    private final FluxProcessor<Notification, Notification> fluxProcessor;
    private final FluxSink<Notification> notificationsSink;

    public FluxNotificationProcessor(FluxProcessor<Notification, Notification> fluxProcessor) {
        this.fluxProcessor = fluxProcessor;
        this.notificationsSink = fluxProcessor.sink();
    }

    public Flux<Notification> stream() {
        return fluxProcessor.map(x -> x);
    }

    public void notify(Notification notification) {
        logger.info("Adding notification to sink");
        notificationsSink.next(notification);
    }
}
