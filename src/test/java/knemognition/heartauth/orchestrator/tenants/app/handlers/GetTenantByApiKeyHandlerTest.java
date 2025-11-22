package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenants.app.utils.KeyHasher;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTenantByApiKeyHandlerTest {

    @Mock
    private TenantStore tenantStore;
    @Mock
    private TenantMapper tenantMapper;
    @Mock
    private KeyHasher keyHasher;

    @InjectMocks
    private GetTenantByApiKeyHandler handler;

    @Captor
    private ArgumentCaptor<String> hashCaptor;

    @Test
    void shouldHashApiKeyAndReturnTenant() {
        Tenant tenant = Tenant.builder()
                .tenantId(UUID.randomUUID())
                .build();
        TenantRead read = TenantRead.builder()
                .tenantId(tenant.getTenantId())
                .build();
        when(keyHasher.handle("api-key")).thenReturn("hash");
        when(tenantStore.getTenantByApiKey("hash")).thenReturn(Optional.of(tenant));
        when(tenantMapper.toReadModel(tenant)).thenReturn(read);

        assertThat(handler.handle("api-key")).contains(read);
        verify(keyHasher).handle(hashCaptor.capture());
        assertThat(hashCaptor.getValue()).isEqualTo("api-key");
    }
}
