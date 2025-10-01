package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;


import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefData;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.EcgRefDataEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EcgRefDataMapper {

    @Mapping(source = "appUser.id", target = "appUserId")
    EcgRefData toDomain(EcgRefDataEntity entity);

    @Mapping(target = "appUser", source = "appUserId")
    EcgRefDataEntity toEntity(EcgRefData domain);

    default UserEntity mapAppUser(UUID id) {
        if (id == null) return null;
        UserEntity u = new UserEntity();
        u.setId(id);
        return u;
    }
}
