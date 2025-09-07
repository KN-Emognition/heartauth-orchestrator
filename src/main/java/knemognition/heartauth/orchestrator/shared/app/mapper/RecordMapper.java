package knemognition.heartauth.orchestrator.shared.app.mapper;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecordMapper {

    default <T> Map<String, String> convertObjectToMap(T source, ObjectMapper objectMapper) {
        return objectMapper.convertValue(source, new TypeReference<>() {});
    }
}