package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi;

import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.CombinedModelActionDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.utils.SetKeyFromHeaderProcessor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class ModelActionStreamConfig {

    private final String requestTopic;
    private final String replyTopic;
    private final String combinedModelTopic;

    public ModelActionStreamConfig(@Value("${model.api.topics.request}") String requestTopic, @Value("${model.api.topics.reply}") String replyTopic, @Value("${model.api.topics.combined}") String combinedModelTopic) {
        this.requestTopic = requestTopic;
        this.replyTopic = replyTopic;
        this.combinedModelTopic = combinedModelTopic;
    }

    @Bean
    public KStream<String, CombinedModelActionDto> modelApiTopology(StreamsBuilder builder) {

        final Serde<String> stringSerde = Serdes.String();

        final JsonSerde<PredictRequestDto> requestSerde = new JsonSerde<>(PredictRequestDto.class);
        final JsonSerde<PredictResponseDto> responseSerde = new JsonSerde<>(PredictResponseDto.class);
        final JsonSerde<CombinedModelActionDto> combinedSerde = new JsonSerde<>(CombinedModelActionDto.class);

        requestSerde.deserializer()
                .ignoreTypeHeaders();
        responseSerde.deserializer()
                .ignoreTypeHeaders();

        combinedSerde.serializer()
                .setAddTypeInfo(true);

        KStream<byte[], PredictRequestDto> requestsRaw = builder.stream(requestTopic,
                Consumed.with(Serdes.ByteArray(), requestSerde));

        KStream<byte[], PredictResponseDto> responsesRaw = builder.stream(replyTopic,
                Consumed.with(Serdes.ByteArray(), responseSerde));

        KStream<String, PredictRequestDto> requestsByCorr = requestsRaw.process(
                () -> new SetKeyFromHeaderProcessor<>(HeaderNames.HEADER_CORRELATION_ID));

        KStream<String, PredictResponseDto> responsesByCorr = responsesRaw.process(
                () -> new SetKeyFromHeaderProcessor<>(HeaderNames.HEADER_CORRELATION_ID));

        JoinWindows windows = JoinWindows.ofTimeDifferenceAndGrace(Duration.ofMinutes(10), Duration.ofMinutes(2));

        KStream<String, CombinedModelActionDto> combined = requestsByCorr.outerJoin(responsesByCorr,
                (req, resp) -> CombinedModelActionDto.builder()
                        .predictRequest(req)
                        .predictResponse(resp)
                        .timestamp(System.currentTimeMillis())
                        .build(), windows, StreamJoined.with(stringSerde, requestSerde, responseSerde));

        combined.to(combinedModelTopic, Produced.with(stringSerde, combinedSerde));
        return combined;
    }
}
