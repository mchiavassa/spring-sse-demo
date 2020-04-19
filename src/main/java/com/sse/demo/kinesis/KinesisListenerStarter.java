package com.sse.demo.kinesis;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.metrics.interfaces.MetricsLevel;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class KinesisListenerStarter implements SmartLifecycle {

    private static final String APPLICATION_NAME = "sse-demo";

    private final RecordProcessorFactory recordProcessorFactory;
    private final String streamName;
    private final String region;
    private final String kinesisEndpoint;
    private final String dynamoDbEndpoint;

    private Thread workerThread;
    private Worker worker;

    public KinesisListenerStarter(
        RecordProcessorFactory recordProcessorFactory,
        @Value("${streams.sse-demo}") String streamName,
        @Value("${aws.region}") String region,
        @Value("${aws.kinesis.endpoint}") String kinesisEndpoint,
        @Value("${aws.dynamodb.endpoint}") String dynamoDbEndpoint
    ) {
        this.recordProcessorFactory = recordProcessorFactory;
        this.streamName = streamName;
        this.region = region;
        this.kinesisEndpoint = kinesisEndpoint;
        this.dynamoDbEndpoint = dynamoDbEndpoint;
    }

    @Override
    public void start() {
        System.out.println("Starting Kinesis Worker");

        KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(
            APPLICATION_NAME,
            streamName,
            getAWSCredentials(),
            APPLICATION_NAME + UUID.randomUUID()
        ).withMetricsLevel(MetricsLevel.NONE)
            .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);

        worker = new Worker.Builder()
            .recordProcessorFactory(recordProcessorFactory)
            .config(config)
            .kinesisClient(AmazonKinesisClientBuilder
                .standard()
                .withCredentials(getAWSCredentials())
                .withEndpointConfiguration(new EndpointConfiguration(kinesisEndpoint, region))
                .build())
            .dynamoDBClient(AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(getAWSCredentials())
                .withEndpointConfiguration(new EndpointConfiguration(dynamoDbEndpoint, region))
                .build())
            .build();

        workerThread = new Thread(worker, "kinesisListener");
        workerThread.start();
    }

    @Override
    public void stop() {
        System.out.println("Stopping Kinesis Worker");

        try {
            worker.createGracefulShutdownCallable().call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        return workerThread != null && workerThread.isAlive();
    }

    private AWSStaticCredentialsProvider getAWSCredentials() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials("fake", "fake"));
    }
}
