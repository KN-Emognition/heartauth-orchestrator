package knemognition.heartauth.orchestrator.internal.app.ports.in;

import knemognition.heartauth.orchestrator.internal.model.ChallengeStatusResponse;

import java.util.UUID;

public interface ChallengeQueryService {
    ChallengeStatusResponse status(UUID challengeId);
}
