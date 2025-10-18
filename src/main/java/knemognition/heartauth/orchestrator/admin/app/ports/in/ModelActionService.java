package knemognition.heartauth.orchestrator.admin.app.ports.in;

import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;

import java.util.UUID;

public interface ModelActionService {
    void handle(UUID correlationId, CombinedModelActionDto modelActionDto);
}
