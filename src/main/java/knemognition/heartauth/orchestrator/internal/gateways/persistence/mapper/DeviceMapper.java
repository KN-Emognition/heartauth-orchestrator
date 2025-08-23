package knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import knemognition.heartauth.orchestrator.external.model.DeviceCredential;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.jpa.entity.DeviceCredentialEntity;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(target = "createdAt", expression = "java( e.getCreatedAt()!=null ? e.getCreatedAt().getEpochSecond() : null )")
    DeviceCredential toApi(DeviceCredentialEntity e);
}
