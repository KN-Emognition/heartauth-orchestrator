package knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.mappers;

import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;
import knemognition.heartauth.orchestrator.firebase.api.RefDataCollectPushMessage;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.model.ChallengePushMessageDto;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.model.MessageTypeDto;
import knemognition.heartauth.orchestrator.firebase.infrastructure.messaging.model.RefDataCollectPushMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {MessageTypeDto.class})
public interface FirebaseMapper {

    @Mapping(target = "type", expression = "java(MessageTypeDto.CHALLENGE)")
    ChallengePushMessageDto toDto(ChallengePushMessage challengePushMessage);

    @Mapping(target = "type", expression = "java(MessageTypeDto.REF_DATA_COLLECT)")
    RefDataCollectPushMessageDto toDto(RefDataCollectPushMessage refDataCollectPushMessage);
}
