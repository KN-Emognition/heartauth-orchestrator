package knemognition.heartauth.orchestrator.ecg.api;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class SaveReferenceDataCmd {
    UUID userId;
    List<List<Float>> refEcg;
}
