package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.EcgEvaluateCmd;
import knemognition.heartauth.orchestrator.ecg.app.ports.AsyncModelApi;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.EcgPayload;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SendEcgEvaluateRequestHandler {
    private final AsyncModelApi asyncModelApi;
    private final EcgStore ecgStore;

    public void handle(EcgEvaluateCmd cmd) {
        log.info("[ECG] Evaluating ECG data...");
        var refEcg = ecgStore.getReferenceEcg(cmd.getUserId())
                .map(RefEcg::getRefEcg)
                .orElseThrow();
        asyncModelApi.predict(cmd.getCorrelationId(), EcgPayload.builder()
                .testEcg(cmd.getTestEcg())
                .refEcg(refEcg)
                .build());
        log.info("[ECG] Ecg data sent.");
    }
}
