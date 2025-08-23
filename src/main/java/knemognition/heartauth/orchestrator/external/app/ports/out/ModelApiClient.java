package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.client.modelapi.In;
import knemognition.heartauth.orchestrator.client.modelapi.PredictResponse;

public interface ModelApiClient {
    PredictResponse predict(In in);
}
