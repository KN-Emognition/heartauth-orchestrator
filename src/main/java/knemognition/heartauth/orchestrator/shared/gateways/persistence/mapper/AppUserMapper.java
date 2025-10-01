package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.AppUser;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AppUserMapper {

    @Mapping(source = "tenant.id", target = "tenantId")
    AppUser toDomain(UserEntity entity);

    @Mapping(target = "tenant", source = "tenantId")
    UserEntity toEntity(AppUser domain);

    default TenantEntity mapTenant(UUID id) {
        if (id == null) return null;
        TenantEntity t = new TenantEntity();
        t.setId(id);
        return t;
    }
}

