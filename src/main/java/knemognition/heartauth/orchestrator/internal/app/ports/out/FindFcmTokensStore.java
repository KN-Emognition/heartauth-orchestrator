package knemognition.heartauth.orchestrator.internal.app.ports.out;

import java.util.List;
import java.util.UUID;

public interface FindFcmTokensStore {
    List<String> getActiveFcmTokens(UUID userId);
}
