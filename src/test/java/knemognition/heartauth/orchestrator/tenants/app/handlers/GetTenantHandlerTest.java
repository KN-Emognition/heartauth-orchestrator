package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
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
class GetTenantHandlerTest {

    @Mock
    private TenantStore tenantStore;
    @Mock
    private TenantMapper tenantMapper;

    @InjectMocks
    private GetTenantHandler handler;

    @Test
    void shouldReturnTenantReadWhenAvailable() {
        UUID tenantId = UUID.randomUUID();
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .build();
        TenantRead read = TenantRead.builder()
                .tenantId(tenantId)
                .build();
        when(tenantStore.get(tenantId)).thenReturn(Optional.of(tenant));
        when(tenantMapper.toReadModel(tenant)).thenReturn(read);

        assertThat(handler.handle(tenantId)).contains(read);
        verify(tenantStore).get(tenantId);
    }
}
