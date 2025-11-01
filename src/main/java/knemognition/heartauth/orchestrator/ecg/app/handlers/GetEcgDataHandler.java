package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.app.mappers.EcgMapper;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetEcgDataHandler {
    private final EcgStore ecgStore;
    private final EcgMapper ecgMapper;

    public RefEcgRead handle(GetRefDataCmd cmd) {
        log.info("[ECG] Evaluating ECG data...");
        var refEcg = ecgStore.getReferenceEcg(cmd.getUserId())
                .orElseThrow();
        return ecgMapper.toRead(refEcg);
    }
}
