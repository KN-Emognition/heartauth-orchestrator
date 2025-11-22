package knemognition.heartauth.orchestrator.tenants.app;

import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.tenants.app.utils.KeyHasher;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import test.config.HeartauthSpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(SpringProfiles.ADMIN)
class TenantsModuleIntegrationTest extends HeartauthSpringBootTest {

    @Autowired
    private TenantsModule tenantsModule;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private TenantApiKeyRepository tenantApiKeyRepository;
    @Autowired
    private KeyHasher keyHasher;

    @BeforeEach
    void cleanDatabase() {
        tenantApiKeyRepository.deleteAll();
        tenantRepository.deleteAll();
    }

    @Test
    void shouldCreateTenantAndExposeReadModels() {
        CreatedTenant created = tenantsModule.create();

        assertThat(tenantRepository.existsByTenantId(created.getTenantId())).isTrue();

        Optional<TenantRead> readById = tenantsModule.get(created.getTenantId());
        assertThat(readById).isPresent();
        assertThat(readById.get().getTenantId()).isEqualTo(created.getTenantId());

        Optional<TenantRead> readByApiKey = tenantsModule.getByApiKey(created.getApiKey().toString());
        assertThat(readByApiKey).isPresent();
        assertThat(readByApiKey.get().getTenantId()).isEqualTo(created.getTenantId());
    }

    @Test
    void shouldPersistHashedApiKeyAlongsideTenant() {
        CreatedTenant created = tenantsModule.create();

        String expectedHash = keyHasher.handle(created.getApiKey().toString());
        TenantApiKeyEntity storedKey = tenantApiKeyRepository.findActiveByHash(expectedHash)
                .orElseThrow(() -> new AssertionError("Expected hashed key to be stored"));

        assertThat(storedKey.getKeyHash()).isEqualTo(expectedHash);
        assertThat(storedKey.getTenant().getTenantId()).isEqualTo(created.getTenantId());
    }
}
