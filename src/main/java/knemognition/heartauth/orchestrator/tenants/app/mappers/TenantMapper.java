package knemognition.heartauth.orchestrator.tenants.app.mappers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantEntity;
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
