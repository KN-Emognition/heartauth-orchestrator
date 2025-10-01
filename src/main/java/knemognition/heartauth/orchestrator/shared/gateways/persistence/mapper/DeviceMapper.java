package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(source = "appUser.id", target = "appUserId")
    Device toDomain(DeviceEntity entity);

    @Mapping(target = "appUser", source = "appUserId")
    DeviceEntity toEntity(Device domain);

    default UserEntity mapAppUser(UUID id) {
        if (id == null) return null;
        UserEntity u = new UserEntity();
        u.setId(id);
        return u;
    }
}
