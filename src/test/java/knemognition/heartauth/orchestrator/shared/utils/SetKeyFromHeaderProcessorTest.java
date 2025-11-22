package knemognition.heartauth.orchestrator.shared.utils;

import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.streams.processor.api.MockProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SetKeyFromHeaderProcessorTest {

    @Test
    void shouldForwardKeyFromHeader() {
        // given
        var context = new MockProcessorContext<String, String>();
        var processor = new SetKeyFromHeaderProcessor<String>("X-Correlation-Id");
        processor.init(context);

        var headers = new RecordHeaders()
                .add("X-Correlation-Id", "abc".getBytes(StandardCharsets.UTF_8));
        var record = new Record<byte[], String>(null, "payload", 0L, headers);

        // when
        processor.process(record);

        // then
        var forwarded = context.forwarded();
        assertThat(forwarded).hasSize(1);

        var out = forwarded.get(0)
                .record();
        assertThat(out.key()).isEqualTo("abc");
        assertThat(out.value()).isEqualTo("payload");
    }

    @Test
    void shouldForwardNullKeyWhenHeaderMissing() {
        // given
        var context = new MockProcessorContext<String, String>();
        var processor = new SetKeyFromHeaderProcessor<String>("missing");
        processor.init(context);

        var record = new Record<byte[], String>(null, "value", 0L, new RecordHeaders());

        // when
        processor.process(record);

        // then
        var forwarded = context.forwarded();
        assertThat(forwarded).hasSize(1);

        var out = forwarded.get(0)
                .record();
        assertThat(out.key()).isNull();
        assertThat(out.value()).isEqualTo("value");
    }
}
