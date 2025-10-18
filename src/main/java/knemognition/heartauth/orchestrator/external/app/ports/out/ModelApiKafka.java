package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;


/**
 * Port for model prediction
 */
public interface ModelApiKafka {
    /**
     * Make a prediction using the model asynchronously
     * @param prediction includes reference data and test data
     */
    void predict(EcgPrediction prediction);
}