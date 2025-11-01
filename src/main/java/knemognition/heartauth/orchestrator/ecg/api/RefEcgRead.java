package knemognition.heartauth.orchestrator.ecg.api;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RefEcgRead {
    List<List<Float>> refEcg;
}


