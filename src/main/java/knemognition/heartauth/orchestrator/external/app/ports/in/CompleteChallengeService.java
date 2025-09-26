package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;

import java.util.UUID;

public interface CompleteChallengeService {
    void complete(UUID challengeId,
                  ChallengeCompleteRequest req
    );
}
