package knemognition.heartauth.orchestrator.admin.config.kafka;

import knemognition.heartauth.orchestrator.shared.config.kafka.ModelApiTopicsProperties;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.shared.utils.SetKeyFromHeaderProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class ModelActionStreamConfig {

    private final ModelApiTopicsProperties modelApiTopicsProperties;

    @Bean
    public KStream<String, CombinedModelActionDto> modelApiTopology(StreamsBuilder builder) {

        final Serde<String> stringSerde = Serdes.String();
        final JsonSerde<PredictRequestDto> requestSerde = new JsonSerde<>(PredictRequestDto.class);
        final JsonSerde<PredictResponseDto> responseSerde = new JsonSerde<>(PredictResponseDto.class);
        final JsonSerde<CombinedModelActionDto> combinedSerde = new JsonSerde<>(CombinedModelActionDto.class);

        KStream<byte[], PredictRequestDto> requestsRaw =
                builder.stream(modelApiTopicsProperties.getRequest(), Consumed.with(Serdes.ByteArray(), requestSerde));

        KStream<byte[], PredictResponseDto> responsesRaw =
                builder.stream(modelApiTopicsProperties.getReply(), Consumed.with(Serdes.ByteArray(), responseSerde));

        KStream<String, PredictRequestDto> requestsByCorr =
                requestsRaw.process(() -> new SetKeyFromHeaderProcessor<>(HeaderNames.HEADER_CORRELATION_ID));

        KStream<String, PredictResponseDto> responsesByCorr =
                responsesRaw.process(() -> new SetKeyFromHeaderProcessor<>(HeaderNames.HEADER_CORRELATION_ID));

        JoinWindows windows = JoinWindows.ofTimeDifferenceAndGrace(
                Duration.ofMinutes(10),
                Duration.ofMinutes(2)
        );

        KStream<String, CombinedModelActionDto> combined =
                requestsByCorr.outerJoin(
                        responsesByCorr,
                        (req, resp) -> CombinedModelActionDto.builder()
                                .predictRequest(req)
                                .predictResponse(resp)
                                .timestamp(System.currentTimeMillis())
                                .build(),
                        windows,
                        StreamJoined.with(stringSerde, requestSerde, responseSerde)
                );

        combined.to(modelApiTopicsProperties.getCombined(), Produced.with(stringSerde, combinedSerde));

        return combined;
    }
}
