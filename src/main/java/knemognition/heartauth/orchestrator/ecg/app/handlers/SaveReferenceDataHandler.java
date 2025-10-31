package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaveReferenceDataHandler {

    private final EcgStore ecgStore;

    public void handle(SaveReferenceDataCmd cmd) {
        log.info("[ECG] Saving reference ECG data for userId: {}", cmd.getUserId());
        ecgStore.saveReferenceEcg(cmd.getUserId(), RefEcg.builder()
                .refEcg(cmd.getRefEcg())
                .build());
    }
}
