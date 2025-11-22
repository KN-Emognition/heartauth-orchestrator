package knemognition.heartauth.orchestrator.pairings.app.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.pairings.api.CreatePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.PairingStatusRead;
import knemognition.heartauth.orchestrator.pairings.api.QrCodeClaims;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.pairings.domain.CreatePairing;
import knemognition.heartauth.orchestrator.pairings.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PairingsMapperTest {

    private PairingsMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new PairingsMapperImpl();
        ReflectionTestUtils.setField(mapper, "objectMapper", new ObjectMapper());
    }

    @Test
    void shouldMapCreatePairingCmdToIdentifiableUser() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        var cmd = CreatePairingCmd.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();

        IdentifiableUserCmd result = mapper.toCmd(cmd);

        assertThat(result.getTenantId()).isEqualTo(tenantId);
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldMapQrClaimsToDomainAndJwtClaimsSet() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID jti = UUID.randomUUID();
        QrCodeClaims claims = mapper.toClaims(CreatePairingCmd.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build(), jti, 500L);
        PairingProperties props = new PairingProperties();
        props.setIssuer("https://issuer.local");
        props.setAudience(List.of("aud"));

        CreatePairing domain = mapper.toDomain(claims, 120L);
        JwtClaimsSet jwtClaimsSet = mapper.toClaimsSet(props, Instant.parse("2024-01-01T00:00:00Z"), claims);

        assertThat(domain.getTenantId()).isEqualTo(tenantId);
        assertThat(domain.getJti()).isEqualTo(jti);
        assertThat(domain.getTtlSeconds()).isEqualTo(120L);
        assertThat(jwtClaimsSet.getIssuer()).hasToString("https://issuer.local");
        assertThat(jwtClaimsSet.getAudience()).containsExactly("aud");
    }

    @Test
    void shouldMapPairingStateToReadModels() {
        PairingState state = PairingState.builder()
                .id(UUID.randomUUID())
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .status(FlowStatus.PENDING)
                .reason("waiting")
                .deviceId("device")
                .displayName("Device")
                .publicKey("pub")
                .fcmToken("token")
                .platform(Platform.IOS)
                .osVersion("17")
                .model("iPhone")
                .nonceB64("nonce")
                .build();

        PairingStatusRead statusRead = mapper.toRead(state);
        DeviceCreate device = mapper.toDevice(state);
        IdentifiableUserCmd user = mapper.toCmd(state);

        assertThat(statusRead.getStatus()).isEqualTo(FlowStatus.PENDING);
        assertThat(device.getDeviceId()).isEqualTo("device");
        assertThat(user.getUserId()).isEqualTo(state.getUserId());
    }

    @Test
    void shouldMapInitPayloadToEnrichDeviceData() {
        UUID pairingId = UUID.randomUUID();
        var cmd = InitPairingCmd.builder()
                .deviceId("device")
                .displayName("name")
                .publicKey("pub")
                .platform(Platform.ANDROID)
                .build();

        EnrichDeviceData enrich = mapper.toEnrichDeviceData(cmd, "nonce", pairingId);

        assertThat(enrich.getJti()).isEqualTo(pairingId);
        assertThat(enrich.getNonceB64()).isEqualTo("nonce");
        assertThat(enrich.getDeviceId()).isEqualTo("device");
    }
}
