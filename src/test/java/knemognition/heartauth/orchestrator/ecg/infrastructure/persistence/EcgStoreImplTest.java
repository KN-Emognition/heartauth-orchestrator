package knemognition.heartauth.orchestrator.ecg.infrastructure.persistence;

import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.entity.EcgRefDataEntity;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.mappers.EcgRefMapper;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.repository.EcgRefDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EcgStoreImplTest {

    @Mock
    private EcgRefDataRepository repository;
    @Mock
    private EcgRefMapper mapper;

    @InjectMocks
    private EcgStoreImpl store;

    @Test
    void shouldReturnMappedReferenceData() {
        UUID userId = UUID.fromString("f7c64bf0-499b-4a53-89e7-49fcd92eb60c");
        var entity = new EcgRefDataEntity();
        entity.setUserId(userId);
        var domain = RefEcg.builder()
                .refEcg(List.of(List.of(0.5f)))
                .build();
        when(repository.findByUserId(userId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        var result = store.getReferenceEcg(userId);

        assertThat(result).contains(domain);
    }

    @Test
    void shouldPersistReferenceData() {
        UUID userId = UUID.fromString("dd9c152b-8b4f-4a4c-9ce8-04ea6dad20d5");
        var domain = RefEcg.builder()
                .refEcg(List.of(List.of(0.11f)))
                .build();

        store.saveReferenceEcg(userId, domain);

        ArgumentCaptor<EcgRefDataEntity> captor = ArgumentCaptor.forClass(EcgRefDataEntity.class);
        verify(repository).save(captor.capture());
        EcgRefDataEntity entity = captor.getValue();
        assertThat(entity.getUserId()).isEqualTo(userId);
        assertThat(entity.getRefEcg()).isEqualTo(domain.getRefEcg());
    }
}
