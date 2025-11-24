package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.CreatePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.CreatedPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.pairings.domain.CreatePairing;
import knemognition.heartauth.orchestrator.pairings.domain.CreatedPairingResult;
import knemognition.heartauth.orchestrator.pairings.api.QrCodeClaims;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class CreatePairingHandlerTest {

    @Mock
    private UserModule userModule;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private PairingsMapper mapper;
    @Mock
    private PairingProperties properties;
    @Mock
    private PairingStore pairingStore;

    @InjectMocks
    private CreatePairingHandler handler;

    @BeforeEach
    void setup() {
        when(properties.getMinTtl()).thenReturn(30);
        when(properties.getMaxTtl()).thenReturn(300);
        when(properties.getDefaultTtl()).thenReturn(120);
    }

    @Test
    void shouldCreatePairingAndEncodeJwt() {
        var cmd = CreatePairingCmd.builder()
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .ttlSeconds(60)
                .build();
        var claims = QrCodeClaims.builder()
                .jti(UUID.randomUUID())
                .tenantId(cmd.getTenantId())
                .build();
        var create = CreatePairing.builder()
                .jti(claims.getJti())
                .tenantId(cmd.getTenantId())
                .build();
        var createdResult = CreatedPairingResult.builder()
                .id(claims.getJti())
                .ttlSeconds(60L)
                .build();
        var read = CreatedPairingRead.builder()
                .jti(claims.getJti())
                .build();

        when(userModule.checkIfUserExists(mapper.toCmd(cmd))).thenReturn(false);
        when(mapper.toClaims(eq(cmd), any(UUID.class), any(Long.class))).thenReturn(claims);
        when(mapper.toClaimsSet(eq(properties), any(Instant.class), eq(claims))).thenReturn(JwtClaimsSet.builder()
                .claim("tenantId", cmd.getTenantId()
                        .toString())
                .build());
        var jwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("jwt-token")
                .header("alg", "ES256")
                .claim("sub", "pairing")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .build();
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        when(mapper.toDomain(claims, 60L)).thenReturn(create);
        when(pairingStore.createPairing(create)).thenReturn(createdResult);
        when(mapper.toRead(claims, "jwt-token")).thenReturn(read);

        CreatedPairingRead result = handler.createPairing(cmd);

        assertThat(result).isEqualTo(read);
        verify(pairingStore).createPairing(create);
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        var cmd = CreatePairingCmd.builder()
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();
        when(userModule.checkIfUserExists(mapper.toCmd(cmd))).thenReturn(true);

        assertThatThrownBy(() -> handler.createPairing(cmd))
                .isInstanceOf(NoPairingException.class);
    }
}
