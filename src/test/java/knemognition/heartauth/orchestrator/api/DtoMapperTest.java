package knemognition.heartauth.orchestrator.api;

import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.PlatformDto;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.CreateChallengeRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.CreateChallengeResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.CreatePairingResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.admin.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.challenges.api.ChallengeStatusRead;
import knemognition.heartauth.orchestrator.challenges.api.CompleteChallengeWithPredictionPayloadCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallengeRead;
import knemognition.heartauth.orchestrator.pairings.api.CompletePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.CreatePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.CreatedPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.api.PairingStatusRead;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingRead;
import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.users.api.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DtoMapperTest {

    private final DtoMapper mapper = Mappers.getMapper(DtoMapper.class);

    @Test
    void shouldMapCreateChallengeRequestToCmd() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateChallengeRequestDto dto = new CreateChallengeRequestDto()
                .userId(userId)
                .ttlSeconds(90);

        CreateChallengeCmd cmd = mapper.toCmd(dto, tenantId);

        assertThat(cmd.getUserId()).isEqualTo(userId);
        assertThat(cmd.getTenantId()).isEqualTo(tenantId);
        assertThat(cmd.getTtlSeconds()).isEqualTo(90);
    }

    @Test
    void shouldHandleNullCreateChallengeRequest() {
        UUID tenantId = UUID.randomUUID();

        CreateChallengeCmd cmd = mapper.toCmd((CreateChallengeRequestDto) null, tenantId);

        assertThat(cmd.getTenantId()).isEqualTo(tenantId);
        assertThat(cmd.getUserId()).isNull();
        assertThat(cmd.getTtlSeconds()).isNull();
    }

    @Test
    void shouldReturnNullIfCreateChallengeInputMissing() {
        assertThat(mapper.toCmd((CreateChallengeRequestDto) null, null)).isNull();
    }

    @Test
    void shouldMapCreatedChallengeToResponse() {
        CreatedChallengeRead read = CreatedChallengeRead.builder()
                .challengeId(UUID.randomUUID())
                .build();

        CreateChallengeResponseDto dto = mapper.toDto(read);

        assertThat(dto.getChallengeId()).isEqualTo(read.getChallengeId());
    }

    @Test
    void shouldReturnNullWhenCreatedChallengeMissing() {
        assertThat(mapper.toDto((CreatedChallengeRead) null)).isNull();
    }

    @Test
    void shouldMapStatusReads() {
        ChallengeStatusRead challengeStatus = ChallengeStatusRead.builder()
                .status(knemognition.heartauth.orchestrator.challenges.api.FlowStatus.APPROVED)
                .reason("ok")
                .build();
        PairingStatusRead pairingStatus = PairingStatusRead.builder()
                .status(FlowStatus.PENDING)
                .reason("pending")
                .build();

        StatusResponseDto challengeDto = mapper.toDto(challengeStatus);
        StatusResponseDto pairingDto = mapper.toDto(pairingStatus);

        assertThat(challengeDto.getStatus().getValue()).isEqualTo("APPROVED");
        assertThat(pairingDto.getStatus().getValue()).isEqualTo("PENDING");
    }

    @ParameterizedTest
    @EnumSource(FlowStatus.class)
    void shouldMapAllPairingStatuses(FlowStatus status) {
        PairingStatusRead read = PairingStatusRead.builder()
                .status(status)
                .reason("meta")
                .build();

        StatusResponseDto dto = mapper.toDto(read);

        assertThat(dto.getStatus().getValue()).isEqualTo(status.name());
        assertThat(dto.getReason()).isEqualTo("meta");
    }

    @ParameterizedTest
    @EnumSource(value = knemognition.heartauth.orchestrator.challenges.api.FlowStatus.class)
    void shouldMapAllChallengeStatuses(knemognition.heartauth.orchestrator.challenges.api.FlowStatus status) {
        ChallengeStatusRead read = ChallengeStatusRead.builder()
                .status(status)
                .reason("info")
                .build();

        StatusResponseDto dto = mapper.toDto(read);

        assertThat(dto.getStatus().getValue()).isEqualTo(status.name());
        assertThat(dto.getReason()).isEqualTo("info");
    }

    @Test
    void shouldReturnNullForNullStatuses() {
        assertThat(mapper.toDto((PairingStatusRead) null)).isNull();
        assertThat(mapper.toDto((ChallengeStatusRead) null)).isNull();
    }

    @Test
    void shouldMapCreatePairingRequestToCmd() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreatePairingRequestDto dto = new CreatePairingRequestDto()
                .userId(userId);

        CreatePairingCmd cmd = mapper.toCmd(dto, tenantId);

        assertThat(cmd.getTenantId()).isEqualTo(tenantId);
        assertThat(cmd.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldHandleNullCreatePairingRequest() {
        UUID tenantId = UUID.randomUUID();

        CreatePairingCmd cmd = mapper.toCmd((CreatePairingRequestDto) null, tenantId);

        assertThat(cmd.getTenantId()).isEqualTo(tenantId);
        assertThat(cmd.getUserId()).isNull();
        assertThat(cmd.getTtlSeconds()).isNull();
    }

    @Test
    void shouldReturnNullIfCreatePairingInputMissing() {
        assertThat(mapper.toCmd((CreatePairingRequestDto) null, null)).isNull();
    }

    @Test
    void shouldMapCreatedPairingToResponse() {
        CreatedPairingRead read = CreatedPairingRead.builder()
                .jti(UUID.randomUUID())
                .jwt("jwt")
                .build();

        CreatePairingResponseDto dto = mapper.toDto(read);

        assertThat(dto.getJti()).isEqualTo(read.getJti());
        assertThat(dto.getJwt()).isEqualTo("jwt");
    }

    @Test
    void shouldReturnNullWhenCreatedPairingMissing() {
        assertThat(mapper.toDto((CreatedPairingRead) null)).isNull();
    }

    @Test
    void shouldMapCompleteChallengeRequest() {
        UUID challengeId = UUID.randomUUID();
        var dto = new CompleteChallengeRequestDto("token", "sig");

        CompleteChallengeWithPredictionPayloadCmd cmd = mapper.toCmd(dto, challengeId);

        assertThat(cmd.getChallengeId()).isEqualTo(challengeId);
        assertThat(cmd.getDataToken()).isEqualTo("token");
    }

    @Test
    void shouldHandleNullCompleteChallengeRequest() {
        UUID challengeId = UUID.randomUUID();

        CompleteChallengeWithPredictionPayloadCmd cmd = mapper.toCmd((CompleteChallengeRequestDto) null, challengeId);

        assertThat(cmd.getChallengeId()).isEqualTo(challengeId);
        assertThat(cmd.getDataToken()).isNull();
        assertThat(cmd.getSignature()).isNull();
    }

    @Test
    void shouldReturnNullIfCompleteChallengeInputMissing() {
        assertThat(mapper.toCmd((CompleteChallengeRequestDto) null, null)).isNull();
    }

    @Test
    void shouldMapCreatedTenantToResponse() {
        var created = CreatedTenant.builder()
                .tenantId(UUID.randomUUID())
                .apiKey(UUID.randomUUID())
                .build();

        CreateTenantResponseDto dto = mapper.toDto(created);

        assertThat(dto.getId()).isEqualTo(created.getTenantId());
        assertThat(dto.getApiKey()).isEqualTo(created.getApiKey());
    }

    @Test
    void shouldReturnNullWhenCreatedTenantMissing() {
        assertThat(mapper.toDto((CreatedTenant) null)).isNull();
    }

    @Test
    void shouldMapInitPairingRequestToCmd() {
        var request = new InitPairingRequestDto(
                "device-1",
                "Pixel 9",
                "public",
                "fcm-token",
                PlatformDto.ANDROID)
                .osVersion("15")
                .model("Pixel 9 Pro");

        InitPairingCmd cmd = mapper.toCmd(request);

        assertThat(cmd.getDeviceId()).isEqualTo("device-1");
        assertThat(cmd.getDisplayName()).isEqualTo("Pixel 9");
        assertThat(cmd.getPublicKey()).isEqualTo("public");
        assertThat(cmd.getFcmToken()).isEqualTo("fcm-token");
        assertThat(cmd.getPlatform()).isEqualTo(Platform.ANDROID);
        assertThat(cmd.getOsVersion()).isEqualTo("15");
        assertThat(cmd.getModel()).isEqualTo("Pixel 9 Pro");
    }

    @Test
    void shouldReturnNullWhenInitPairingRequestMissing() {
        assertThat(mapper.toCmd((InitPairingRequestDto) null)).isNull();
    }

    @Test
    void shouldMapInitPairingReadToResponse() {
        var read = InitPairingRead.builder()
                .nonce("nonce")
                .expiresAt(1234L)
                .build();

        InitPairingResponseDto dto = mapper.toDto(read);

        assertThat(dto.getNonce()).isEqualTo("nonce");
        assertThat(dto.getExpiresAt()).isEqualTo(1234L);
    }

    @Test
    void shouldReturnNullWhenInitPairingReadMissing() {
        assertThat(mapper.toDto((InitPairingRead) null)).isNull();
    }

    @Test
    void shouldMapCompletePairingRequestToCmd() {
        var request = new CompletePairingRequestDto("token", "signature");

        CompletePairingCmd cmd = mapper.toCmd(request);

        assertThat(cmd.getDataToken()).isEqualTo("token");
        assertThat(cmd.getSignature()).isEqualTo("signature");
    }

    @Test
    void shouldReturnNullWhenCompletePairingRequestMissing() {
        assertThat(mapper.toCmd((CompletePairingRequestDto) null)).isNull();
    }
}
