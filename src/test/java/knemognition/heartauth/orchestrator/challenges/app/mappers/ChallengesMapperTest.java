package knemognition.heartauth.orchestrator.challenges.app.mappers;

import knemognition.heartauth.orchestrator.challenges.api.*;
import knemognition.heartauth.orchestrator.challenges.domain.*;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.utils.KeyLoader;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengesMapperTest {

    private final ChallengesMapper mapper = Mappers.getMapper(ChallengesMapper.class);
    private static final UUID USER_ID = UUID.fromString("a3c62b9c-e4e1-4105-8b0f-937533ecb366");
    private static final UUID TENANT_ID = UUID.fromString("f0e88f76-2de0-4ffd-92e9-fc0a1ff35ed0");
    private static final UUID CHALLENGE_ID = UUID.fromString("18715465-84b4-4b3c-8e8c-ba93c3cd09a1");

    @Test
    void shouldMapCreateChallengeCmdToIdentifiableUser() {
        var cmd = CreateChallengeCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .ttlSeconds(30)
                .build();

        IdentifiableUserCmd result = mapper.toCmd(cmd);

        assertThat(result.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void shouldMapChallengeStateToRead() {
        var state = ChallengeState.builder()
                .id(CHALLENGE_ID)
                .tenantId(TENANT_ID)
                .status(FlowStatus.APPROVED)
                .reason("ok")
                .build();

        ChallengeStatusRead read = mapper.toRead(state);

        assertThat(read.getStatus()).isEqualTo(FlowStatus.APPROVED);
        assertThat(read.getReason()).isEqualTo("ok");
    }

    @Test
    void shouldMapCompleteChallengePayloadToValidateNonce() {
        var payload = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(CHALLENGE_ID)
                .signature("sig")
                .build();
        var state = ChallengeState.builder()
                .nonceB64("nonce")
                .userPublicKey("pub")
                .build();

        ValidateNonceCmd cmd = mapper.toCmd(payload, state);

        assertThat(cmd.getNonce()).isEqualTo("nonce");
        assertThat(cmd.getSignature()).isEqualTo("sig");
        assertThat(cmd.getPub()).isEqualTo("pub");
    }

    @Test
    void shouldMapChallengeStateToIdentifiableUser() {
        var state = ChallengeState.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();

        IdentifiableUserCmd result = mapper.toCmd(state);

        assertThat(result.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    void shouldMapChallengeCreationToDomain() throws Exception {
        CreateChallengeCmd cmd = CreateChallengeCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .ttlSeconds(55)
                .build();
        KeyPair keyPair = createKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        String userPublicKey = KeyLoader.toPem(keyPair.getPublic(), "PUBLIC KEY");

        CreateChallenge domain = mapper.toDomain(cmd, "nonce-123", 40, privateKey, userPublicKey);

        assertThat(domain.getTenantId()).isEqualTo(TENANT_ID);
        assertThat(domain.getUserId()).isEqualTo(USER_ID);
        assertThat(domain.getNonceB64()).isEqualTo("nonce-123");
        assertThat(domain.getTtlSeconds()).isEqualTo(55L);
        assertThat(domain.getEphemeralPrivateKey()).isEqualTo(KeyLoader.toPem(privateKey, "PRIVATE KEY"));
        assertThat(domain.getUserPublicKey()).isEqualTo(userPublicKey);
    }

    @Test
    void shouldMapCreatedChallengeResultToRead() {
        CreatedChallengeResult created = CreatedChallengeResult.builder()
                .id(CHALLENGE_ID)
                .ttlSeconds(60L)
                .build();

        CreatedChallengeRead read = mapper.toRead(created);

        assertThat(read.getChallengeId()).isEqualTo(CHALLENGE_ID);
    }

    @Test
    void shouldMapToChallengePushMessage() throws Exception {
        CreatedChallengeResult result = CreatedChallengeResult.builder()
                .id(CHALLENGE_ID)
                .ttlSeconds(75L)
                .exp(Instant.now().plusSeconds(75).getEpochSecond())
                .build();
        KeyPair pair = createKeyPair();
        PublicKey publicKey = pair.getPublic();

        ChallengePushMessage message = mapper.toChallengePushMessage(result, "nonce-abc", publicKey);

        assertThat(message.getChallengeId()).isEqualTo(CHALLENGE_ID);
        assertThat(message.getNonce()).isEqualTo("nonce-abc");
        assertThat(message.getTtl()).isEqualTo(75L);
        assertThat(message.getPublicKey()).isEqualTo(KeyLoader.toPem(publicKey, "PUBLIC KEY"));
        assertThat(message.getType()).isEqualTo(MessageType.CHALLENGE);
    }

    private KeyPair createKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        return generator.generateKeyPair();
    }
}
