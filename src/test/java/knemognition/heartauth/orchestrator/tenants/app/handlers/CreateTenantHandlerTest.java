package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenants.app.utils.KeyHasher;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTenantHandlerTest {

    @Mock
    private TenantStore tenantStore;
    @Mock
    private KeyHasher keyHasher;

    @InjectMocks
    private CreateTenantHandler handler;

    @Captor
    private ArgumentCaptor<Tenant> tenantCaptor;
    @Captor
    private ArgumentCaptor<TenantApiKey> apiKeyCaptor;
    @Captor
    private ArgumentCaptor<String> hashInputCaptor;

    @Test
    void shouldPersistTenantAndReturnCreatedTenant() {
        when(keyHasher.handle(anyString())).thenReturn("hash");

        CreatedTenant created = handler.handle();

        assertThat(created.getTenantId()).isNotNull();
        assertThat(created.getApiKey()).isNotNull();
        verify(keyHasher).handle(hashInputCaptor.capture());
        assertThat(hashInputCaptor.getValue()).isEqualTo(created.getApiKey().toString());
        verify(tenantStore).createTenantWithApiKey(tenantCaptor.capture(), apiKeyCaptor.capture());
        assertThat(tenantCaptor.getValue().getTenantId()).isEqualTo(created.getTenantId());
        assertThat(apiKeyCaptor.getValue().getKeyHash()).isEqualTo("hash");
    }
}
