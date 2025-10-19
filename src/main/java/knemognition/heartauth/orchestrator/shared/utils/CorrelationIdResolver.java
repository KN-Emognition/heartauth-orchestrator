package knemognition.heartauth.orchestrator.shared.utils;

import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CorrelationIdResolver {

    public static UUID resolveOrThrow(ConsumerRecord<String, String> rec) {
        String raw = MDC.get(HeaderNames.MDC_CORRELATION_ID);
        if (isBlank(raw)) raw = rec.key();
        if (isBlank(raw)) {
            Header h = rec.headers()
                    .lastHeader(HeaderNames.HEADER_CORRELATION_ID);
            if (h != null) raw = new String(h.value(), StandardCharsets.UTF_8);
        }
        if (isBlank(raw)) {
            throw new IllegalStateException("Missing correlationId in MDC, record key, and header");
        }
        return UUID.fromString(raw);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim()
                .isEmpty();
    }
}
