package com.sse.demo.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import org.springframework.stereotype.Component;

@Component
public class RecordProcessorFactory implements IRecordProcessorFactory {

    private final RecordProcessor recordProcessor;

    public RecordProcessorFactory(RecordProcessor recordProcessor) {
        this.recordProcessor = recordProcessor;
    }

    @Override
    public IRecordProcessor createProcessor() {
        return recordProcessor;
    }
}
