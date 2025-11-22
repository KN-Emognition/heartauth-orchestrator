package knemognition.heartauth.orchestrator.challenges.infrastructure.persistence;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.api.NoChallengeException;
import knemognition.heartauth.orchestrator.challenges.domain.*;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.entity.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.mappers.ChallengeStoreMapper;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.repository.ChallengeStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengeStoreImplTest {

    private static final UUID FLOW_ID = UUID.fromString("381635ed-0305-4d89-90f5-bf4d96dc8025");
    private static final UUID TENANT_ID = UUID.fromString("dd9dd56f-1b9b-4cf8-947b-801e584cb36a");
    private static final UUID USER_ID = UUID.fromString("956dfef3-666c-4b5c-9a1d-bc1ae9f5d767");
    private static final UUID CORRELATION_ID = UUID.fromString("a73dc64a-697b-4a82-998d-4e5ed75f0b3b");

    @Mock
    private ChallengeStateRepository repository;
    @Mock
    private ChallengeStoreMapper mapper;

    @InjectMocks
    private ChallengeStoreImpl challengeStore;

    private ChallengeStateRedis activeEntity;

    @BeforeEach
    void setup() {
        long now = Instant.now()
                .getEpochSecond();
        activeEntity = ChallengeStateRedis.builder()
                .id(UUID.randomUUID())
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .status(FlowStatus.CREATED)
                .exp(now + 60)
                .reason(null)
                .createdAt(now)
                .ttlSeconds(60L)
                .build();
    }

    @Test
    void shouldReturnMappedStateWhenFlowExistsAndNotExpired() {
        var redis = ChallengeStateRedis.builder()
                .id(FLOW_ID)
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .status(FlowStatus.CREATED)
                .exp(Instant.now()
                        .getEpochSecond() + 120)
                .build();
        var mapped = ChallengeState.builder()
                .id(FLOW_ID)
                .status(FlowStatus.CREATED)
                .build();
        when(repository.findById(FLOW_ID)).thenReturn(Optional.of(redis));
        when(mapper.toDomain(redis)).thenReturn(mapped);

        Optional<ChallengeState> result = challengeStore.getFlow(FLOW_ID);

        assertThat(result).contains(mapped);
    }

    @Test
    void shouldReturnEmptyWhenFlowExpired() {
        var redis = ChallengeStateRedis.builder()
                .id(FLOW_ID)
                .exp(Instant.now()
                        .getEpochSecond() - 1)
                .build();
        when(repository.findById(FLOW_ID)).thenReturn(Optional.of(redis));

        Optional<ChallengeState> result = challengeStore.getFlow(FLOW_ID);

        assertThat(result).isEmpty();
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void shouldCreateChallengeAndMarkPreviousActiveAsDenied() {
        CreateChallenge create = CreateChallenge.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .nonceB64("nonce")
                .ttlSeconds(45L)
                .ephemeralPrivateKey("pem")
                .userPublicKey("pub")
                .build();
        ChallengeStateRedis newEntity = ChallengeStateRedis.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        CreatedChallengeResult createdResult = CreatedChallengeResult.builder()
                .id(FLOW_ID)
                .ttlSeconds(45L)
                .build();
        when(repository.findAllByTenantIdAndUserIdOrderByCreatedAtDesc(TENANT_ID, USER_ID)).thenReturn(List.of(activeEntity));
        when(mapper.fromCreate(create)).thenReturn(newEntity);
        when(mapper.toCreatedResult(newEntity)).thenReturn(createdResult);

        CreatedChallengeResult result = challengeStore.createChallenge(create);

        assertThat(result).isEqualTo(createdResult);
        assertThat(newEntity.getReason()).isEqualTo(FlowStatusReason.FLOW_RECREATED);
        assertThat(activeEntity.getStatus()).isEqualTo(FlowStatus.DENIED);
        assertThat(activeEntity.getReason()).isEqualTo(FlowStatusReason.FLOW_DENIED_BY_RECREATING_FLOW);
        verify(repository).save(activeEntity);
        verify(repository).save(newEntity);
    }

    @Test
    void shouldCreateChallengeWithCreatedReasonWhenNoActives() {
        CreateChallenge create = CreateChallenge.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .nonceB64("nonce")
                .ttlSeconds(60L)
                .ephemeralPrivateKey("pem")
                .userPublicKey("pub")
                .build();
        ChallengeStateRedis newEntity = ChallengeStateRedis.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        CreatedChallengeResult createdResult = CreatedChallengeResult.builder()
                .id(FLOW_ID)
                .ttlSeconds(60L)
                .build();
        when(repository.findAllByTenantIdAndUserIdOrderByCreatedAtDesc(TENANT_ID, USER_ID)).thenReturn(List.of());
        when(mapper.fromCreate(create)).thenReturn(newEntity);
        when(mapper.toCreatedResult(newEntity)).thenReturn(createdResult);

        CreatedChallengeResult result = challengeStore.createChallenge(create);

        assertThat(result).isEqualTo(createdResult);
        assertThat(newEntity.getReason()).isEqualTo(FlowStatusReason.FLOW_CREATED);
        verify(repository).save(newEntity);
    }

    @Test
    void shouldReturnChallengeByCorrelationId() {
        ChallengeStateRedis entity = ChallengeStateRedis.builder()
                .id(FLOW_ID)
                .modelApiTryId(CORRELATION_ID)
                .build();
        ChallengeState mapped = ChallengeState.builder()
                .id(FLOW_ID)
                .build();
        when(repository.findFirstByModelApiTryIdOrderByCreatedAtDesc(CORRELATION_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(mapped);

        ChallengeState state = challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID);

        assertThat(state).isEqualTo(mapped);
    }

    @Test
    void shouldThrowWhenChallengeByCorrelationIdMissing() {
        when(repository.findFirstByModelApiTryIdOrderByCreatedAtDesc(CORRELATION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> challengeStore.getChallengeStateByCorrelationId(CORRELATION_ID))
                .isInstanceOf(NoChallengeException.class);
    }

    @Test
    void shouldSetStatusWhenFlowExists() {
        ChallengeStateRedis entity = ChallengeStateRedis.builder()
                .id(FLOW_ID)
                .build();
        when(repository.findById(FLOW_ID)).thenReturn(Optional.of(entity));
        StatusChange change = StatusChange.builder()
                .id(FLOW_ID)
                .status(FlowStatus.APPROVED)
                .reason("ok")
                .build();

        boolean updated = challengeStore.setStatus(change);

        assertThat(updated).isTrue();
        verify(mapper).applyStatus(entity, FlowStatus.APPROVED, "ok");
        verify(repository).save(entity);
    }

    @Test
    void shouldReturnFalseWhenSettingStatusForMissingFlow() {
        StatusChange change = StatusChange.builder()
                .id(FLOW_ID)
                .status(FlowStatus.APPROVED)
                .reason("none")
                .build();
        when(repository.findById(FLOW_ID)).thenReturn(Optional.empty());

        boolean updated = challengeStore.setStatus(change);

        assertThat(updated).isFalse();
        verify(mapper, never()).applyStatus(any(), any(), any());
        verify(repository, never()).save(any());
    }
}
