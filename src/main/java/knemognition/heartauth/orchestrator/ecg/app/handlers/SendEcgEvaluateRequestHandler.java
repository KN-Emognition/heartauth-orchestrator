package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.EcgEvaluateCmd;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiSendApi;
import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendEcgEvaluateRequestHandler {
    private final ModelApiSendApi asyncModelApi;
    private final EcgStore ecgStore;

    public void handle(EcgEvaluateCmd cmd) {
        log.info("[ECG] Evaluating ECG data...");
        var refEcg = ecgStore.getReferenceEcg(cmd.getUserId())
                .map(RefEcg::getRefEcg)
                .orElseThrow();
        asyncModelApi.handle(EcgSendPredictCmd.builder()
                .correlationId(cmd.getCorrelationId())
                .testEcg(cmd.getTestEcg())
                .refEcg(refEcg)
                .build());
        log.info("[ECG] Ecg data sent.");
    }
}
