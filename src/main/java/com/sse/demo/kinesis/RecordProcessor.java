package com.sse.demo.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.sse.demo.consumers.KinesisConsumer;
import com.sse.demo.services.FluxNotificationProcessor;
import com.sse.demo.models.Notification;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RecordProcessor implements IRecordProcessor {

    private final KinesisConsumer kinesisConsumer;
    private final FluxNotificationProcessor notificationProcessor;

    public RecordProcessor(
        KinesisConsumer kinesisConsumer,
        FluxNotificationProcessor notificationProcessor
    ) {
        this.kinesisConsumer = kinesisConsumer;
        this.notificationProcessor = notificationProcessor;
    }

    @Override
    public void initialize(InitializationInput initializationInput) {
        System.out.println("Initializing record processor");
    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        System.out.println(
            String.format(
                "Processing %d records from Kinesis",
                processRecordsInput.getRecords().size()
            )
        );

        processRecordsInput.getRecords().forEach(record -> {
            String message = StandardCharsets.UTF_8.decode(record.getData()).toString();
            kinesisConsumer.processMessage(message);
            notificationProcessor.notify(new Notification(UUID.randomUUID().toString(), message));
        });

        checkpoint(processRecordsInput.getCheckpointer());
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        if (shutdownInput.getShutdownReason().equals(ShutdownReason.TERMINATE)) {
            checkpoint(shutdownInput.getCheckpointer());
        }

        System.out.println("Shutting down record processor");
    }

    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
        try {
            checkpointer.checkpoint();
        } catch (InvalidStateException | ShutdownException e) {
            e.printStackTrace();
        }
    }
}
