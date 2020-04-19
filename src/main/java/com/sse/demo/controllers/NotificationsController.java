package com.sse.demo.controllers;

import com.sse.demo.services.FluxNotificationProcessor;
import com.sse.demo.models.Notification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
public class NotificationsController {

    private final FluxNotificationProcessor notificationsProcessor;

    public NotificationsController(FluxNotificationProcessor notificationsProcessor) {
        this.notificationsProcessor = notificationsProcessor;
    }

    @GetMapping(path = "/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> stream() {
        return notificationsProcessor.stream();
    }
}
