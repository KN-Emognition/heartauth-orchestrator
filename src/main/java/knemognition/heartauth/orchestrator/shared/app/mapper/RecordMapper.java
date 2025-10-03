package knemognition.heartauth.orchestrator.shared.app.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class RecordMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    public <T> Map<String, String> convertObjectToMap(T source) {
        return objectMapper.convertValue(source, new TypeReference<>() {
        });
    }
}