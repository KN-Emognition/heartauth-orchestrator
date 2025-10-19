package knemognition.heartauth.orchestrator.shared.config.kafka;

import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.RecordInterceptor;

import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class KafkaMdcInterceptorConfig {

    @Bean
    public RecordInterceptor<Object, Object> mdcCorrelationInterceptor() {
        return new RecordInterceptor<>() {
            @Override
            public ConsumerRecord<Object, Object> intercept(ConsumerRecord<Object, Object> record,
                                                            Consumer<Object, Object> consumer) {
                String corr = extractHeader(record, HeaderNames.HEADER_CORRELATION_ID);
                if (corr != null) {
                    MDC.put(HeaderNames.MDC_CORRELATION_ID, corr);
                }
                return record;
            }

            @Override
            public void success(ConsumerRecord<Object, Object> record, Consumer<Object, Object> consumer) {
                MDC.remove(HeaderNames.MDC_CORRELATION_ID);
            }

            @Override
            public void failure(ConsumerRecord<Object, Object> record,
                                Exception exception,
                                Consumer<Object, Object> consumer) {
                MDC.remove(HeaderNames.MDC_CORRELATION_ID);
            }
        };
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            RecordInterceptor<Object, Object> mdcCorrelationInterceptor) {

        var factory = new ConcurrentKafkaListenerContainerFactory<Object, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordInterceptor(mdcCorrelationInterceptor);
        return factory;
    }

    private static String extractHeader(ConsumerRecord<?, ?> record, String name) {
        Header h = record.headers()
                .lastHeader(name);
        if (h == null) h = record.headers()
                .lastHeader(name.toLowerCase());
        return h != null ? new String(h.value(), StandardCharsets.UTF_8) : null;
    }
}
