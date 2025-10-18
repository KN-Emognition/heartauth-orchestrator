package knemognition.heartauth.orchestrator.shared.utils;


import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class KafkaCorrelationId {


    public static String ensure(String maybeCorr) {
        return (maybeCorr != null && !maybeCorr.isBlank()) ? maybeCorr : UUID.randomUUID()
                .toString();
    }

    public static void put(Headers headers, String corr) {
        headers.remove(HeaderNames.HEADER_CORRELATION_ID);
        headers.add(HeaderNames.HEADER_CORRELATION_ID, corr.getBytes(StandardCharsets.UTF_8));
    }
}
