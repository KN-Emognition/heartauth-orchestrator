package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey; // <-- use the API key domain
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TenantApiKeyMapper {

    @Mapping(source = "tenant.id", target = "tenantId")
    TenantApiKey toDomain(TenantApiKeyEntity entity);

    @Mapping(target = "tenant", source = "tenantId")
    TenantApiKeyEntity toEntity(TenantApiKey domain);

    default TenantEntity mapTenant(UUID id) {
        if (id == null) return null;
        TenantEntity t = new TenantEntity();
        t.setId(id);
        return t;
    }
}
