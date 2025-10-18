package knemognition.heartauth.orchestrator.external.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;

import java.util.UUID;

/**
 * Port for model prediction
 */
public interface ModelApiKafka {
    /**
     * Make a payload using the model asynchronously
     * @param payload includes reference data and test data
     */
    void predict(UUID correlationId, EcgPayload payload);
}