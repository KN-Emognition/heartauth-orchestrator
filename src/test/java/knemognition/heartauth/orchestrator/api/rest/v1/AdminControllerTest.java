package knemognition.heartauth.orchestrator.api.rest.v1;

import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.admin.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiModule;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(SpringProfiles.ADMIN)
@TestPropertySource(properties = "admin.api.key=test-admin-key")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TenantsModule tenantsModule;
    @MockitoBean
    private ModelApiModule modelApiModule;
    @MockitoBean
    private UserModule userModule;
    @MockitoBean
    private EcgModule ecgModule;
    @MockitoBean
    private DtoMapper dtoMapper;

    @Test
    void shouldRegisterTenant() throws Exception {
        var created = CreatedTenant.builder()
                .tenantId(UUID.randomUUID())
                .apiKey(UUID.randomUUID())
                .build();
        var response = new CreateTenantResponseDto();
        response.setId(created.getTenantId());
        response.setApiKey(created.getApiKey());
        when(tenantsModule.create()).thenReturn(created);
        when(dtoMapper.toDto(created)).thenReturn(response);

        mockMvc.perform(post("/admin/v1/tenants"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(created.getTenantId()
                        .toString()))
                .andExpect(jsonPath("$.apiKey").value(created.getApiKey()
                        .toString()));
    }

    @Test
    void shouldReturnModelActions() throws Exception {
        when(modelApiModule.getCombinedModelApi()).thenReturn(Map.of("entries", 2));

        mockMvc.perform(get("/admin/v1/model-action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entries").value(2));
    }
}
