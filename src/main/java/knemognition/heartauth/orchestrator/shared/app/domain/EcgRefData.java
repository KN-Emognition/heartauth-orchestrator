package knemognition.heartauth.orchestrator.shared.app.domain;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class EcgRefData {
    UUID id;
    UUID appUserId;
    List<List<Float>> ecgData;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
