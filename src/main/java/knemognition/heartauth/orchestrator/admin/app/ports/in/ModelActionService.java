package knemognition.heartauth.orchestrator.admin.app.ports.in;

import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.ModelActionDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;

import java.util.List;
import java.util.UUID;

public interface ModelActionService {
    void handle(UUID correlationId, CombinedModelActionDto modelActionDto);

    List<ModelActionDto> read();
}
