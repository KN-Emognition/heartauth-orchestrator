package knemognition.heartauth.orchestrator.external.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefData;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.EcgRefDataEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalMainStoreMapper {

    EcgRefData toDomain(EcgRefDataEntity e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    EcgRefDataEntity toEntity(EcgRefData data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    DeviceEntity toEntity(Device data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    UserEntity toEntity(IdentifiableUser user);
}
