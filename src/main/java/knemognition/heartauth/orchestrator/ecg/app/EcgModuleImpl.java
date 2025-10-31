package knemognition.heartauth.orchestrator.ecg.app;

import knemognition.heartauth.orchestrator.ecg.api.EcgEvaluateCmd;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.ecg.app.handlers.SaveReferenceDataHandler;
import knemognition.heartauth.orchestrator.ecg.app.handlers.SendEcgEvaluateRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcgModuleImpl implements EcgModule {
    private final SendEcgEvaluateRequestHandler sendEcgEvaluateRequestHandler;
    private final SaveReferenceDataHandler saveReferenceDataHandler;

    @Override
    public void saveReferenceData(SaveReferenceDataCmd cmd) {
        saveReferenceDataHandler.handle(cmd);
    }

    @Override
    public void sendEcgEvaluateRequest(EcgEvaluateCmd cmd) {
        sendEcgEvaluateRequestHandler.handle(cmd);
    }
}
