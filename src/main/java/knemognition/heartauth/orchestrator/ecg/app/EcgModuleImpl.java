package knemognition.heartauth.orchestrator.ecg.app;

import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.ecg.app.handlers.GetEcgDataHandler;
import knemognition.heartauth.orchestrator.ecg.app.handlers.SaveReferenceDataHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcgModuleImpl implements EcgModule {
    private final GetEcgDataHandler getEcgDataHandler;
    private final SaveReferenceDataHandler saveReferenceDataHandler;

    @Override
    public void saveReferenceData(SaveReferenceDataCmd cmd) {
        saveReferenceDataHandler.handle(cmd);
    }

    @Override
    public RefEcgRead getUserReferenceData(GetRefDataCmd cmd) {
        return getEcgDataHandler.handle(cmd);
    }
}
