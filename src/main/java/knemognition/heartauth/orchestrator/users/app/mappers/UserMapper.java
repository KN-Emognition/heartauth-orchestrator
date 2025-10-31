package knemognition.heartauth.orchestrator.users.app.mappers;

import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    DeviceRead toReadModel(DeviceEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "appUser", ignore = true)
    DeviceEntity toEntity(DeviceCreate data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "devices", ignore = true)
    UserEntity toEntity(IdentifiableUserCmd user);

    UserRead toReadModel(UserEntity entity);
}
