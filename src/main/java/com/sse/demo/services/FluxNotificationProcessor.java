package com.sse.demo.services;

import com.sse.demo.models.Notification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

public class FluxNotificationProcessor {

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
        notificationsSink.next(notification);
    }
}
