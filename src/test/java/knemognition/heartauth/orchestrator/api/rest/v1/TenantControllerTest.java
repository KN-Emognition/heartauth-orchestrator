package knemognition.heartauth.orchestrator.api.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.*;
import knemognition.heartauth.orchestrator.challenges.api.ChallengeStatusRead;
import knemognition.heartauth.orchestrator.challenges.api.ChallengesModule;
import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallengeRead;
import knemognition.heartauth.orchestrator.pairings.api.CreatePairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.CreatedPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.PairingStatusRead;
import knemognition.heartauth.orchestrator.pairings.api.PairingsModule;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TenantController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(SpringProfiles.TENANT)
class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private DtoMapper dtoMapper;
    @MockitoBean
    private PairingsModule pairingsModule;
    @MockitoBean
    private ChallengesModule challengesModule;

    @Test
    void shouldCreateChallenge() throws Exception {
        UUID tenantId = UUID.randomUUID();
        var request = new CreateChallengeRequestDto().userId(UUID.randomUUID())
                .ttlSeconds(60);
        var cmd = CreateChallengeCmd.builder()
                .tenantId(tenantId)
                .userId(request.getUserId())
                .ttlSeconds(60)
                .build();
        var created = CreatedChallengeRead.builder()
                .challengeId(UUID.randomUUID())
                .build();
        var response = new CreateChallengeResponseDto().challengeId(created.getChallengeId());
        when(dtoMapper.toCmd(request, tenantId)).thenReturn(cmd);
        when(challengesModule.create(cmd)).thenReturn(created);
        when(dtoMapper.toDto(created)).thenReturn(response);

        mockMvc.perform(post("/internal/v1/challenge").requestAttr(HeaderNames.ATTR_TENANT_ID, tenantId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.challengeId").value(created.getChallengeId()
                        .toString()));
    }

    @Test
    void shouldReturnChallengeStatus() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID challengeId = UUID.randomUUID();
        var status = ChallengeStatusRead.builder()
                .status(knemognition.heartauth.orchestrator.challenges.api.FlowStatus.APPROVED)
                .build();
        var dto = new StatusResponseDto().status(
                knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.FlowStatusDto.APPROVED);
        when(challengesModule.getStatus(any())).thenReturn(status);
        when(dtoMapper.toDto(status)).thenReturn(dto);

        mockMvc.perform(get("/internal/v1/challenge/status/{id}", challengeId).requestAttr(HeaderNames.ATTR_TENANT_ID,
                        tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldCreatePairing() throws Exception {
        UUID tenantId = UUID.randomUUID();
        var request = new CreatePairingRequestDto().userId(UUID.randomUUID());
        var cmd = CreatePairingCmd.builder()
                .tenantId(tenantId)
                .userId(request.getUserId())
                .build();
        var created = CreatedPairingRead.builder()
                .jti(UUID.randomUUID())
                .jwt("jwt")
                .build();
        var dto = new CreatePairingResponseDto().jti(created.getJti())
                .jwt(created.getJwt());
        when(dtoMapper.toCmd(request, tenantId)).thenReturn(cmd);
        when(pairingsModule.create(cmd)).thenReturn(created);
        when(dtoMapper.toDto(created)).thenReturn(dto);

        mockMvc.perform(post("/internal/v1/pairing").requestAttr(HeaderNames.ATTR_TENANT_ID, tenantId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jwt").value("jwt"));
    }

    @Test
    void shouldReturnPairingStatus() throws Exception {
        UUID tenantId = UUID.randomUUID();
        UUID jti = UUID.randomUUID();
        var status = PairingStatusRead.builder()
                .status(knemognition.heartauth.orchestrator.pairings.api.FlowStatus.PENDING)
                .build();
        var dto = new StatusResponseDto().status(
                knemognition.heartauth.orchestrator.api.rest.v1.tenants.model.FlowStatusDto.PENDING);
        when(pairingsModule.getStatus(any())).thenReturn(status);
        when(dtoMapper.toDto(status)).thenReturn(dto);

        mockMvc.perform(get("/internal/v1/pairing/status/{jti}", jti).requestAttr(HeaderNames.ATTR_TENANT_ID, tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
