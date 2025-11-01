package knemognition.heartauth.orchestrator.ecg.api;

import org.springframework.modulith.NamedInterface;

@NamedInterface
public interface EcgModule {

    void saveReferenceData(SaveReferenceDataCmd cmd);

    RefEcgRead getUserReferenceData(GetRefDataCmd cmd);
}
