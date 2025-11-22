package knemognition.heartauth.orchestrator.api.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.challenges.api.ChallengesModule;
import knemognition.heartauth.orchestrator.challenges.api.CompleteChallengeWithPredictionPayloadCmd;
import knemognition.heartauth.orchestrator.pairings.api.CompletePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.PairingsModule;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MobileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(SpringProfiles.MOBILE)
class MobileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChallengesModule challengesModule;
    @MockitoBean
    private PairingsModule pairingsModule;
    @MockitoBean
    private DtoMapper dtoMapper;

    @TestConfiguration
    static class EcKeyConfig {
        @Bean
        ECKey publicJwk() throws Exception {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
            generator.initialize(new ECGenParameterSpec("secp256r1"));
            KeyPair pair = generator.generateKeyPair();
            return new ECKey.Builder(Curve.P_256, (ECPublicKey) pair.getPublic()).keyID("test-kid")
                    .build();
        }
    }

    @Test
    void completeChallengeShouldReturnNoContentWhenApproved() throws Exception {
        UUID challengeId = UUID.randomUUID();
        var dto = new CompleteChallengeRequestDto("jwt", "sig");
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(challengeId)
                .dataToken("jwt")
                .signature("sig")
                .build();
        when(dtoMapper.toCmd(dto, challengeId)).thenReturn(cmd);
        when(challengesModule.complete(cmd)).thenReturn(true);

        mockMvc.perform(post("/external/v1/challenge/{id}/complete", challengeId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void completeChallengeShouldReturnBadRequestWhenRejected() throws Exception {
        UUID challengeId = UUID.randomUUID();
        var dto = new CompleteChallengeRequestDto("jwt", "sig");
        var cmd = CompleteChallengeWithPredictionPayloadCmd.builder()
                .challengeId(challengeId)
                .dataToken("jwt")
                .signature("sig")
                .build();
        when(dtoMapper.toCmd(dto, challengeId)).thenReturn(cmd);
        when(challengesModule.complete(cmd)).thenReturn(false);

        mockMvc.perform(post("/external/v1/challenge/{id}/complete", challengeId).contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void completePairingShouldInvokeModule() throws Exception {
        var dto = new CompletePairingRequestDto("token", "sig");
        var cmd = CompletePairingCmd.builder()
                .dataToken("token")
                .signature("sig")
                .build();
        when(dtoMapper.toCmd(dto)).thenReturn(cmd);

        mockMvc.perform(post("/external/v1/pairing/complete").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        verify(pairingsModule).complete(cmd);
    }

    @Test
    void initPairingShouldReturnResponse() throws Exception {
        var dto = new InitPairingRequestDto().deviceId("device")
                .displayName("Device")
                .publicKey("PEM")
                .fcmToken("fcm-token")
                .platform(knemognition.heartauth.orchestrator.api.rest.v1.mobile.model.PlatformDto.ANDROID);
        var cmd = InitPairingCmd.builder()
                .deviceId("device")
                .build();
        var response = new InitPairingResponseDto().nonce("nonce")
                .expiresAt(1234L);
        when(dtoMapper.toCmd(dto)).thenReturn(cmd);
        when(pairingsModule.init(cmd)).thenReturn(
                knemognition.heartauth.orchestrator.pairings.api.InitPairingRead.builder()
                        .nonce("nonce")
                        .expiresAt(1234L)
                        .build());
        when(dtoMapper.toDto(ArgumentMatchers.any(
                knemognition.heartauth.orchestrator.pairings.api.InitPairingRead.class))).thenReturn(response);

        mockMvc.perform(post("/external/v1/pairing/init").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nonce").value("nonce"));
    }

    @Test
    void getWellKnownShouldExposeJwk() throws Exception {
        mockMvc.perform(get("/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys[0].kty").value("EC"))
                .andExpect(jsonPath("$.keys[0].kid").value("test-kid"));
    }
}
