package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.mapper.CreatePairingMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.internal.config.pairing.InternalPairingProperties;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import test.config.HeartauthUnitTest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreatePairingServiceImplTest extends HeartauthUnitTest {

    @Mock
    private CreateFlowStore<CreatePairing> store;
    @Mock
    private CreatePairingMapper mapper;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private InternalPairingProperties props;

    private CreatePairingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CreatePairingServiceImpl(store, mapper, jwtEncoder, props);

        when(props.getIssuer()).thenReturn("https://issuer.example");
        when(props.getAudience()).thenReturn(List.of("aud1"));
        when(props.getMinTtl()).thenReturn(60);      // 1 minute
        when(props.getMaxTtl()).thenReturn(3600);    // 1 hour
        when(props.getDefaultTtl()).thenReturn(600); // 10 minutes

        when(jwtEncoder.encode(any())).thenAnswer(inv -> Jwt.withTokenValue("signed.jwt.value")
                .header("alg", SignatureAlgorithm.ES256)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .subject("ignored-for-token")
                .build());

        when(mapper.toCreatePairing(any(), any(), anyInt())).thenReturn(null);
    }


    private PairingCreateRequest req(UUID userId, Integer ttlSeconds) {
        return PairingCreateRequest.builder()
                .userId(userId)
                .ttlSeconds(ttlSeconds)
                .build();
    }

    @Test
    @DisplayName("Encodes JWT with ES256, correct claims; maps & stores; returns JTI and token")
    void happyPath_usesProvidedTtlWhenInsideBounds() {
        // given
        UUID userId = UUID.fromString("aaaaaaaa-aaaa-4aaa-aaaa-aaaaaaaaaaaa");
        int requestedTtl = 300; // within [min=60, max=3600]
        Instant before = Instant.now();

        ArgumentCaptor<JwtEncoderParameters> paramsCap = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        ArgumentCaptor<PairingCreateRequest> reqCap = ArgumentCaptor.forClass(PairingCreateRequest.class);
        ArgumentCaptor<UUID> jtiCap = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Integer> ttlCap = ArgumentCaptor.forClass(Integer.class);

        // when
        PairingCreateResponse out = service.create(req(userId, requestedTtl));
        Instant after = Instant.now();

        // then: encoder called once; capture claims/headers
        verify(jwtEncoder).encode(paramsCap.capture());
        CapturedJwt jwt = new CapturedJwt(paramsCap.getValue());

        // Headers
        assertThat(jwt.headers.getAlgorithm()).isEqualTo(SignatureAlgorithm.ES256);

        // Claims
        assertThat(jwt.claims.getSubject()).isEqualTo(userId.toString());
        assertThat(jwt.claims.getIssuer()).hasToString("https://issuer.example");
        assertThat(jwt.claims.getAudience()).containsExactly("aud1");

        Instant iat = jwt.claims.getIssuedAt();
        Instant exp = jwt.claims.getExpiresAt();
        assertThat(iat).isBetween(before.minusSeconds(1), after.plusSeconds(1));
        assertThat(exp).isEqualTo(iat.plusSeconds(requestedTtl));

        // Mapper called with (req, jti, effectiveTtl)
        verify(mapper).toCreatePairing(reqCap.capture(), jtiCap.capture(), ttlCap.capture());
        PairingCreateRequest passedReq = reqCap.getValue();
        assertThat(passedReq.getUserId()).isEqualTo(userId);
        assertThat(passedReq.getTtlSeconds()).isEqualTo(requestedTtl);
        assertThat(ttlCap.getValue()).isEqualTo(requestedTtl);

        // Store is invoked with whatever mapper returned
        verify(store).create(any());

        // Response mirrors JTI and token
        assertThat(out).isNotNull();
        assertThat(out.getJti()).isEqualTo(jtiCap.getValue());
        // Your generated model appears to expose 'jwt' as the token field:
        assertThat(out.getJwt()).isEqualTo("signed.jwt.value");
    }

    private static class CapturedJwt {
        final JwsHeader headers;
        final JwtClaimsSet claims;

        CapturedJwt(JwtEncoderParameters params) {
            this.headers = params.getJwsHeader();
            this.claims = params.getClaims();
        }
    }

    @Nested
    class TtlClamping {

        @Test
        @DisplayName("TTL below min ⇒ clamped to min")
        void ttlBelowMin() {
            UUID userId = UUID.fromString("bbbbbbbb-bbbb-4bbb-bbbb-bbbbbbbbbbbb");
            int belowMin = 10;        // < min=60
            int expectedTtl = 60;     // min

            ArgumentCaptor<JwtEncoderParameters> paramsCap = ArgumentCaptor.forClass(JwtEncoderParameters.class);
            ArgumentCaptor<PairingCreateRequest> reqCap = ArgumentCaptor.forClass(PairingCreateRequest.class);

            service.create(req(userId, belowMin));

            verify(jwtEncoder).encode(paramsCap.capture());
            CapturedJwt jwt = new CapturedJwt(paramsCap.getValue());

            assertThat(jwt.claims.getExpiresAt())
                    .isEqualTo(jwt.claims.getIssuedAt().plusSeconds(expectedTtl));

            // Mapper gets effective TTL; original request is preserved
            verify(mapper).toCreatePairing(reqCap.capture(), any(UUID.class), eq(expectedTtl));
            assertThat(reqCap.getValue().getUserId()).isEqualTo(userId);
            assertThat(reqCap.getValue().getTtlSeconds()).isEqualTo(belowMin);
        }

        @Test
        @DisplayName("TTL null ⇒ uses default")
        void ttlNullUsesDefault() {
            UUID userId = UUID.fromString("cccccccc-cccc-4ccc-cccc-cccccccccccc");
            Integer requested = null; // default=600
            int expectedTtl = 600;

            ArgumentCaptor<JwtEncoderParameters> paramsCap = ArgumentCaptor.forClass(JwtEncoderParameters.class);
            ArgumentCaptor<PairingCreateRequest> reqCap = ArgumentCaptor.forClass(PairingCreateRequest.class);

            service.create(req(userId, requested));

            verify(jwtEncoder).encode(paramsCap.capture());
            CapturedJwt jwt = new CapturedJwt(paramsCap.getValue());
            assertThat(jwt.claims.getExpiresAt())
                    .isEqualTo(jwt.claims.getIssuedAt().plusSeconds(expectedTtl));

            verify(mapper).toCreatePairing(reqCap.capture(), any(UUID.class), eq(expectedTtl));
            assertThat(reqCap.getValue().getUserId()).isEqualTo(userId);
            assertThat(reqCap.getValue().getTtlSeconds()).isNull();
        }

        @Test
        @DisplayName("TTL above max ⇒ clamped to max")
        void ttlAboveMax() {
            UUID userId = UUID.fromString("dddddddd-dddd-4ddd-dddd-dddddddddddd");
            int aboveMax = 7200;      // > max=3600
            int expectedTtl = 3600;   // max

            ArgumentCaptor<JwtEncoderParameters> paramsCap = ArgumentCaptor.forClass(JwtEncoderParameters.class);
            ArgumentCaptor<PairingCreateRequest> reqCap = ArgumentCaptor.forClass(PairingCreateRequest.class);

            service.create(req(userId, aboveMax));

            verify(jwtEncoder).encode(paramsCap.capture());
            CapturedJwt jwt = new CapturedJwt(paramsCap.getValue());
            assertThat(jwt.claims.getExpiresAt())
                    .isEqualTo(jwt.claims.getIssuedAt().plusSeconds(expectedTtl));

            verify(mapper).toCreatePairing(reqCap.capture(), any(UUID.class), eq(expectedTtl));
            assertThat(reqCap.getValue().getUserId()).isEqualTo(userId);
            assertThat(reqCap.getValue().getTtlSeconds()).isEqualTo(aboveMax);
        }
    }
}
