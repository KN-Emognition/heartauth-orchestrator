package knemognition.heartauth.orchestrator.tenants.app;

import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.app.handlers.CreateTenantHandler;
import knemognition.heartauth.orchestrator.tenants.app.handlers.GetTenantByApiKeyHandler;
import knemognition.heartauth.orchestrator.tenants.app.handlers.GetTenantHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantsModuleImplTest {

    @Mock
    private CreateTenantHandler createHandler;
    @Mock
    private GetTenantHandler getHandler;
    @Mock
    private GetTenantByApiKeyHandler apiKeyHandler;

    @InjectMocks
    private TenantsModuleImpl module;

    @Test
    void shouldDelegateCreate() {
        var created = CreatedTenant.builder().tenantId(UUID.randomUUID()).apiKey(UUID.randomUUID()).build();
        when(createHandler.handle()).thenReturn(created);

        assertThat(module.create()).isEqualTo(created);
        verify(createHandler).handle();
    }

    @Test
    void shouldDelegateGet() {
        UUID tenantId = UUID.randomUUID();
        var read = TenantRead.builder().tenantId(tenantId).build();
        when(getHandler.handle(tenantId)).thenReturn(Optional.of(read));

        assertThat(module.get(tenantId)).contains(read);
    }

    @Test
    void shouldDelegateGetByApiKey() {
        var read = TenantRead.builder().tenantId(UUID.randomUUID()).build();
        when(apiKeyHandler.handle("key")).thenReturn(Optional.of(read));

        assertThat(module.getByApiKey("key")).contains(read);
    }
}
