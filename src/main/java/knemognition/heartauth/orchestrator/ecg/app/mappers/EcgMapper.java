package knemognition.heartauth.orchestrator.ecg.app.mappers;

import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EcgMapper {
    RefEcgRead toRead(RefEcg src);
}
