package knemognition.heartauth.orchestrator.challenges.app.ports;


import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;

import java.util.UUID;

/**
 * Port for model prediction
 */
public interface ModelApiProducer {
    /**
     * Make a payload using the model asynchronously
     * @param payload includes reference data and test data
     */
    void predict(UUID id, EcgPayload payload);
}