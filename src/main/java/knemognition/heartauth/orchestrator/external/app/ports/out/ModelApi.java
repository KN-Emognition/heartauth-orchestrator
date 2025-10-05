package knemognition.heartauth.orchestrator.external.app.ports.out;


import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;

/**
 * Port for model prediction
 */
public interface ModelApi {
    /**
     * @param prediction includes reference data and test data
     * @return true if the model predicts positive
     */
    boolean predict(EcgPrediction prediction);
}