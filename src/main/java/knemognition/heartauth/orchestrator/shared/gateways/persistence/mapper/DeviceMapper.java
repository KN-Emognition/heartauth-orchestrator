package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device toDomain(DeviceEntity entity);
}
