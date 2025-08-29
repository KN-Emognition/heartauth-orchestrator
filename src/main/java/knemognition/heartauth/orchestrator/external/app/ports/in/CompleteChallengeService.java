package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.StatusResponse;

import java.util.UUID;

public interface CompleteChallengeService {
    StatusResponse complete(UUID challengeId, ChallengeCompleteRequest req, String dpopHeader);
}
