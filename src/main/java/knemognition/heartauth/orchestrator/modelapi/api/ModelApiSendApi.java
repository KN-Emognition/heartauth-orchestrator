package knemognition.heartauth.orchestrator.modelapi.api;


/**
 * Port for model prediction
 */
public interface ModelApiSendApi {
    /**
     * Make a payload using the model asynchronously
     * @param payload includes reference data and test data
     */
    void handle(EcgSendPredictCmd payload);
}