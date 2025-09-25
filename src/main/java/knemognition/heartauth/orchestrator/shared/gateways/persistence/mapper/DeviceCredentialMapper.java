package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DeviceCredentialMapper {
    DeviceCredential toDomain(DeviceCredentialEntity e);
    DeviceCredentialEntity toEntity(DeviceCredential d);
}
