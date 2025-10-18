package knemognition.heartauth.orchestrator.external.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;

/**
 * Port for model prediction
 */
public interface ModelApiRest {
    /**
     * @param payload includes reference data and test data
     * @return true if the model predicts positive
     */
    boolean predict(EcgPayload payload);
}