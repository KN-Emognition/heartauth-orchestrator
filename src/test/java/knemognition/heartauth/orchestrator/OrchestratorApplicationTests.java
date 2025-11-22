package knemognition.heartauth.orchestrator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantRepository;
import knemognition.heartauth.orchestrator.tenants.app.utils.KeyHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import test.config.HeartauthSpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles({SpringProfiles.MOBILE, SpringProfiles.ADMIN})
class OrchestratorApplicationTests extends HeartauthSpringBootTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantApiKeyRepository tenantApiKeyRepository;
    @Autowired
    private KeyHasher keyHasher;
    @Autowired
    private ECKey pairingPublicJwk;

    @BeforeEach
    void clearDatabase() {
        tenantApiKeyRepository.deleteAll();
        tenantRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        // smoke test to ensure context boots with admin + mobile profiles
    }

    @Test
    void shouldRegisterTenantThroughAdminApi() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/v1/tenants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HeaderNames.HEADER_API_KEY, "test-admin-key"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.apiKey").isNotEmpty())
                .andReturn();

        JsonNode payload = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID tenantId = UUID.fromString(payload.get("id").asText());
        String apiKey = payload.get("apiKey").asText();

        assertThat(tenantRepository.existsByTenantId(tenantId)).isTrue();
        String hash = keyHasher.handle(apiKey);
        assertThat(tenantApiKeyRepository.findActiveByHash(hash)).isPresent();
    }

    @Test
    void shouldExposeMobileWellKnownJwk() throws Exception {
        mockMvc.perform(get("/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys[0].kid").value(pairingPublicJwk.getKeyID()))
                .andExpect(jsonPath("$.keys[0].kty").value("EC"))
                .andExpect(jsonPath("$.keys[0].crv").value("P-256"))
                .andExpect(jsonPath("$.keys[0].use").value("sig"))
                .andExpect(jsonPath("$.keys[0].alg").value("ES256"))
                .andExpect(jsonPath("$.keys[0].x").value(pairingPublicJwk.getX().toString()))
                .andExpect(jsonPath("$.keys[0].y").value(pairingPublicJwk.getY().toString()));
    }
}
