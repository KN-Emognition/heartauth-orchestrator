package knemognition.heartauth.orchestrator.external.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExternalChallengeStoreMapper {

    FlowStatusDescription toStatus(ChallengeStateRedis src);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     FlowStatus status,
                     String reason);
}
