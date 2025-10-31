package knemognition.heartauth.orchestrator.ecg.app.ports;



import knemognition.heartauth.orchestrator.ecg.domain.EcgPayload;

import java.util.UUID;

/**
 * Port for model prediction
 */
public interface AsyncModelApi {
    /**
     * Make a payload using the model asynchronously
     * @param payload includes reference data and test data
     */
    void predict(UUID correlationId, EcgPayload payload);
}