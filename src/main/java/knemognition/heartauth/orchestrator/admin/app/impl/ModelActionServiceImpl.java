package knemognition.heartauth.orchestrator.admin.app.impl;

import knemognition.heartauth.orchestrator.admin.app.mapper.ModelActionMapper;
import knemognition.heartauth.orchestrator.admin.app.ports.in.ModelActionService;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModelActionServiceImpl implements  ModelActionService {

    private final ModelActionMapper mapper;


    @Override
    public void handle(UUID correlationId, CombinedModelActionDto modelActionDto) {
        // TODO create store, Key: value, serialize full CombinedModelActionDto as value with correlationId as key
    }
}
