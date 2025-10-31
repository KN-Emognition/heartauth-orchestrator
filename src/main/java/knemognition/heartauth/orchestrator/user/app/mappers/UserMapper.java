package knemognition.heartauth.orchestrator.user.app.mappers;

import knemognition.heartauth.orchestrator.user.api.DeviceRead;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.user.domain.Device;
import knemognition.heartauth.orchestrator.user.domain.EcgRefData;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity.EcgRefDataEntity;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Device toDomain(DeviceEntity entity);

    DeviceRead toReadModel(DeviceEntity entity);

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
    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "ecgRefData", ignore = true)
    UserEntity toEntity(IdentifiableUserCmd user);
}
