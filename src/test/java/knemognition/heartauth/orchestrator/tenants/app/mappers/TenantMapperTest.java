package knemognition.heartauth.orchestrator.tenants.app.mappers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TenantMapperTest {

    private final TenantMapper mapper = Mappers.getMapper(TenantMapper.class);

    @Test
    void shouldMapTenantEntityToReadModel() {
        UUID tenantId = UUID.randomUUID();
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .id(UUID.randomUUID())
                .build();

        TenantRead read = mapper.toReadModel(tenant);

        assertThat(read.getTenantId()).isEqualTo(tenantId);
    }

    @Test
    void shouldMapTenantDomainToEntityAndBack() {
        Tenant tenant = Tenant.builder()
                .tenantId(UUID.randomUUID())
                .build();

        TenantEntity entity = mapper.toEntity(tenant);
        entity.setId(UUID.randomUUID());

        Tenant back = mapper.toDomain(entity);

        assertThat(back.getTenantId()).isEqualTo(tenant.getTenantId());
    }

    @Test
    void shouldMapTenantApiKeyToEntity() {
        TenantApiKey apiKey = TenantApiKey.builder()
                .keyHash("hash")
                .build();

        TenantApiKeyEntity entity = mapper.toEntity(apiKey);

        assertThat(entity.getKeyHash()).isEqualTo("hash");
    }
}
