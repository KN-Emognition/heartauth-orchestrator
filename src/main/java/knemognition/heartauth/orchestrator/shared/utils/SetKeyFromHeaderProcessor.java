package knemognition.heartauth.orchestrator.shared.utils;


import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.Record;

/**
 * Modern Kafka Streams Processor API implementation that sets the record key
 * from a specific Kafka header (e.g. "X-Correlation-Id").
 */
public class SetKeyFromHeaderProcessor<V> extends ContextualProcessor<byte[], V, String, V> {

    private final String headerName;

    public SetKeyFromHeaderProcessor(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public void process(Record<byte[], V> record) {
        Headers headers = record.headers();
        Header header = headers != null ? headers.lastHeader(headerName) : null;

        if (header == null) {
            this.context().forward(record.withKey(null));
            return;
        }

        String correlationId = new String(header.value());
        this.context().forward(record.withKey(correlationId));
    }
}
