package com.sse.demo.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KinesisConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KinesisConsumer.class);

    public void processMessage(String message) {
        logger.info("Message received from Kinesis: " + message);
    }
}
