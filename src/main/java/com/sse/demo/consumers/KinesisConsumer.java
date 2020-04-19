package com.sse.demo.consumers;

import org.springframework.stereotype.Component;

@Component
public class KinesisConsumer {

    public void processMessage(String message) {
        System.out.println("Message received from Kinesis: " + message);
    }
}
