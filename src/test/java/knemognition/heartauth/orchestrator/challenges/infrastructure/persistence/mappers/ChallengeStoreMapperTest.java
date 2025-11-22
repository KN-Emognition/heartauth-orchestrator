package knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.mappers;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.entity.ChallengeStateRedis;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengeStoreMapperTest {

    private final ChallengeStoreMapper mapper = Mappers.getMapper(ChallengeStoreMapper.class);

    @Test
    void shouldPopulateGeneratedFieldsWhenCreatingEntity() {
        CreateChallenge create = CreateChallenge.builder()
                .tenantId(UUID.fromString("d96bd631-06e2-4fbe-b481-a1130a94288f"))
                .userId(UUID.fromString("df363964-c6e1-4f4a-8ffb-a5bd5b62ef2d"))
                .nonceB64("nonce")
                .userPublicKey("pub")
                .ephemeralPrivateKey("pem")
                .ttlSeconds(120L)
                .build();
        long before = Instant.now()
                .getEpochSecond();

        ChallengeStateRedis entity = mapper.fromCreate(create);

        long after = Instant.now()
                .getEpochSecond();
        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getModelApiTryId()).isNotNull();
        assertThat(entity.getTenantId()).isEqualTo(create.getTenantId());
        assertThat(entity.getUserId()).isEqualTo(create.getUserId());
        assertThat(entity.getStatus()).isEqualTo(FlowStatus.CREATED);
        assertThat(entity.getCreatedAt()).isBetween(before, after);
        assertThat(entity.getTtlSeconds()).isEqualTo(create.getTtlSeconds());
        assertThat(entity.getExp()).isEqualTo(entity.getCreatedAt() + create.getTtlSeconds());
    }

    @Test
    void shouldMapEntityToDomainState() {
        ChallengeStateRedis entity = ChallengeStateRedis.builder()
                .id(UUID.fromString("a0ef4288-6ddd-42bc-97cf-11b0b6811d24"))
                .tenantId(UUID.fromString("fe0bda7c-93d5-4d0e-89da-9be3e6f7dd48"))
                .userId(UUID.fromString("4fa2bd73-7f38-4b3f-9b38-ff9f49440f10"))
                .status(FlowStatus.PENDING)
                .reason("waiting")
                .ephemeralPrivateKey("pem")
                .userPublicKey("pub")
                .nonceB64("nonce")
                .modelApiTryId(UUID.randomUUID())
                .createdAt(5L)
                .ttlSeconds(120L)
                .exp(125L)
                .build();

        ChallengeState state = mapper.toDomain(entity);

        assertThat(state.getId()).isEqualTo(entity.getId());
        assertThat(state.getTenantId()).isEqualTo(entity.getTenantId());
        assertThat(state.getStatus()).isEqualTo(FlowStatus.PENDING);
        assertThat(state.getReason()).isEqualTo("waiting");
        assertThat(state.getEphemeralPrivateKey()).isEqualTo("pem");
        assertThat(state.getNonceB64()).isEqualTo("nonce");
        assertThat(state.getTtlSeconds()).isEqualTo(120L);
    }

    @Test
    void shouldMapEntityToCreatedResult() {
        ChallengeStateRedis entity = ChallengeStateRedis.builder()
                .id(UUID.fromString("b004943f-7dd3-4d95-87c6-4c20dc077b5a"))
                .ttlSeconds(42L)
                .exp(1000L)
                .build();

        CreatedChallengeResult result = mapper.toCreatedResult(entity);

        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getTtlSeconds()).isEqualTo(42L);
        assertThat(result.getExp()).isEqualTo(1000L);
    }

    @Test
    void shouldApplyStatusChanges() {
        ChallengeStateRedis entity = ChallengeStateRedis.builder()
                .status(FlowStatus.CREATED)
                .reason("old")
                .build();

        mapper.applyStatus(entity, FlowStatus.APPROVED, "approved");

        assertThat(entity.getStatus()).isEqualTo(FlowStatus.APPROVED);
        assertThat(entity.getReason()).isEqualTo("approved");
    }
}
