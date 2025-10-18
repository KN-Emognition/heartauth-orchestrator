package knemognition.heartauth.orchestrator.shared.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /** Fire-and-forget send — asynchronous by default */
    public void send(ProducerRecord<String, String> record) {
        kafkaTemplate.send(record);
    }

    /** Optional overload for simple (topic, message) use */
    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
