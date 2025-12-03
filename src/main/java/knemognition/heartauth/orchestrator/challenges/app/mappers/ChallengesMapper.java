package knemognition.heartauth.orchestrator.challenges.app.mappers;

import knemognition.heartauth.orchestrator.challenges.api.ChallengeStatusRead;
import knemognition.heartauth.orchestrator.challenges.api.CompleteChallengeWithPredictionPayloadCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallengeRead;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.security.PrivateKey;
import java.security.PublicKey;

@Mapper(componentModel = "spring", imports = {KeyLoader.class})
public interface ChallengesMapper {

    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.userPublicKey")
    ValidateNonceCmd toCmd(CompleteChallengeWithPredictionPayloadCmd req, ChallengeState state);

    ChallengeStatusRead toRead(ChallengeState state);

    @Mapping(target = "challengeId", source = "id")
    @Mapping(target = "ttl", source = "ttlSeconds")
    CreatedChallengeRead toRead(CreatedChallengeResult src);

    IdentifiableUserCmd toCmd(CreateChallengeCmd src);

    IdentifiableUserCmd toCmd(ChallengeState src);

    @Mapping(target = "ephemeralPrivateKey", expression = "java(KeyLoader.toPem(privateKey,\"PRIVATE KEY\"))")
    CreateChallenge toDomain(CreateChallengeCmd cmd, String nonceB64, Integer ttlSeconds, PrivateKey privateKey, String userPublicKey);

    @Mapping(target = "challengeId", source = "res.id")
    @Mapping(target = "ttl", source = "res.ttlSeconds")
    @Mapping(target = "publicKey", expression = "java(KeyLoader.toPem(publicKey,\"PUBLIC KEY\"))")
    ChallengePushMessage toChallengePushMessage(CreatedChallengeResult res, String nonce, PublicKey publicKey);
}
