package knemognition.heartauth.orchestrator.admin.app.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.admin.app.mapper.ModelActionMapper;
import knemognition.heartauth.orchestrator.admin.app.ports.in.ModelActionService;
import knemognition.heartauth.orchestrator.admin.app.ports.out.ModelActionStore;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.ModelActionDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelActionServiceImpl implements ModelActionService {

    private final ModelActionStore store;
    private final ObjectMapper objectMapper;
    private final ModelActionMapper mapper;

    @Override
    public void handle(UUID correlationId, CombinedModelActionDto modelActionDto) {
        try {
            String json = objectMapper.writeValueAsString(modelActionDto);
            store.storeModelAction(correlationId, json);
            log.info("Stored CombinedModelActionDto with correlationId={}", correlationId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize CombinedModelActionDto for correlationId={}", correlationId, e);
            throw new IllegalStateException("Unable to serialize CombinedModelActionDto", e);
        }
    }

    @Override
    public List<ModelActionDto> read() {
        return store.getModelActions()
                .stream()
                .map(modelAction -> {
                    try {
                        return mapper.toDto(modelAction.getCorrelationId(),
                                objectMapper.readValue(modelAction.getPayload(), CombinedModelActionDto.class));
                    } catch (Exception e) {
                        log.error("Failed to deserialize payload for id={}", modelAction.getCorrelationId(), e);
                        return null; // skip or handle differently
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
