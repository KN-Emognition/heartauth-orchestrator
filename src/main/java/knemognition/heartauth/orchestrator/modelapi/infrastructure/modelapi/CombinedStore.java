package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi;

import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.CombinedModelActionDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.ModelActionStreamConfig.COMBINED_STORE_NAME;

@Component
@RequiredArgsConstructor
public class CombinedStore {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public Map<String, Object> getStoreAsMap() {

        ReadOnlyKeyValueStore<String, CombinedModelActionDto> store = Objects.requireNonNull(
                        streamsBuilderFactoryBean.getKafkaStreams())
                .store(StoreQueryParameters.fromNameAndType(COMBINED_STORE_NAME, QueryableStoreTypes.keyValueStore()));

        Map<String, Object> result = new HashMap<>();

        try (KeyValueIterator<String, CombinedModelActionDto> all = store.all()) {
            while (all.hasNext()) {
                KeyValue<String, CombinedModelActionDto> entry = all.next();
                result.put(entry.key, entry.value);
            }
        }

        return result;
    }
}
