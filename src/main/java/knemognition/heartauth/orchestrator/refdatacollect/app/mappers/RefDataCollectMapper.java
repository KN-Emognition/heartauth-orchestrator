package knemognition.heartauth.orchestrator.refdatacollect.app.mappers;

import knemognition.heartauth.orchestrator.refdatacollect.api.CreateRefDataCollectProcessCmd;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import org.mapstruct.Mapper;

@Mapper
public interface RefDataCollectMapper {
    IdentifiableUserCmd toCmd(CreateRefDataCollectProcessCmd source);
}
