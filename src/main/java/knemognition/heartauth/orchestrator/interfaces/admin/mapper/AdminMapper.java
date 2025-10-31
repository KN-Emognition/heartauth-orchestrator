package knemognition.heartauth.orchestrator.interfaces.admin.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.ModelActionDto;
import knemognition.heartauth.orchestrator.modelaction.api.ModelActionRead;
import knemognition.heartauth.orchestrator.tenant.api.CreatedTenant;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AdminMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public abstract CreateTenantResponseDto toDto(CreatedTenant createdTenant);

    public List<ModelActionDto> toDtoList(List<ModelActionRead> modelActionReads) {
        if (modelActionReads == null || modelActionReads.isEmpty()) {
            return Collections.emptyList();
        }
        return modelActionReads.stream()
                .map(this::parseModelAction)
                .toList();
    }

    private ModelActionDto parseModelAction(ModelActionRead domain) {
        try {
            return objectMapper.readValue(domain.getPayload(), ModelActionDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid payload for ModelActionRead id=" + domain.getCorrelationId(),
                    e);
        }
    }
}
