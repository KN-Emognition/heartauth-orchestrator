package knemognition.heartauth.orchestrator.tenant.app.mappers;

import knemognition.heartauth.orchestrator.tenant.api.TenantRead;
import knemognition.heartauth.orchestrator.tenant.domain.Tenant;
import knemognition.heartauth.orchestrator.tenant.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.enitity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.enitity.TenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "apiKeys", ignore = true)
    TenantEntity toEntity(Tenant tenant);

    Tenant toDomain(TenantEntity tenantEntity);

    TenantRead toReadModel(Tenant tenant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    TenantApiKeyEntity toEntity(TenantApiKey tenant);
}
