package knemognition.heartauth.orchestrator.internal.interfaces.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.CreatePairingService;
import knemognition.heartauth.orchestrator.internal.app.service.PairingStatusServiceImpl;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("internal")
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(
        controllers = InternalPairingController.class,
        excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.REGEX,
                pattern = "knemognition\\.heartauth\\.orchestrator\\.shared\\..*")
)
class InternalPairingControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PairingStatusServiceImpl pairingStateStatusService;
    @MockitoBean
    CreatePairingService createPairingService;

    @MockitoBean
    ChallengeStateRepository challengeStateRepository; // <-- satisfies ChallengeStatusStoreImpl

    @Test
    void status_returns200_withBody_whenAuthorized() throws Exception {
        UUID jti = UUID.randomUUID();

        // Build whatever your StatusResponse looks like
        StatusResponse serviceResponse = StatusResponse.builder()
                .status(FlowStatus.PENDING)
                .build();

        Mockito.when(pairingStateStatusService.status(eq(jti)))
                .thenReturn(serviceResponse);

        mvc.perform(get("/internal/v1/pairing/{jti}/status", jti) // <-- align with PairingApi route
                        .header("X-KC-Session", "session-123")            // <-- align header name used in PairingApi
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("keycloak"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.state").value("ACTIVE"));

        Mockito.verify(pairingStateStatusService).status(jti);
    }

    @Test
    void create_returns201_withBody_whenAuthorized() throws Exception {
        PairingCreateRequest req = PairingCreateRequest.builder()
                // set fields as your model requires
                .build();

        PairingCreateResponse resp = PairingCreateResponse.builder()
                // set fields you expect back, e.g. id/token/etc
                .build();

        Mockito.when(createPairingService.create(any()))
                .thenReturn(resp);

        mvc.perform(post("/internal/v1/pairing") // <-- align with PairingApi route for "create"
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("keycloak")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // You can assert fields, e.g.: .andExpect(jsonPath("$.id").value("..."));

        Mockito.verify(createPairingService).create(any(PairingCreateRequest.class));
    }

    @Test
    void unauthorized_withoutAuthority() throws Exception {
        UUID jti = UUID.randomUUID();

        // No user / no jwt -> expect 401 by default
        mvc.perform(get("/internal/v1/pairing/{jti}/status", jti)
                        .header("X-KC-Session", "session-123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
        // has a user but missing the required 'keycloak' authority
    void forbidden_withoutRequiredAuthority() throws Exception {
        UUID jti = UUID.randomUUID();

        mvc.perform(get("/internal/v1/pairing/{jti}/status", jti)
                        .header("X-KC-Session", "session-123"))
                .andExpect(status().isForbidden());
    }
}
