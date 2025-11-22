package knemognition.heartauth.orchestrator.ecg.app.mappers;

import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EcgMapperTest {

    private final EcgMapper mapper = Mappers.getMapper(EcgMapper.class);

    @Test
    void shouldMapDomainToReadModel() {
        RefEcg refEcg = RefEcg.builder()
                .refEcg(List.of(List.of(0.1f, 0.2f)))
                .build();

        RefEcgRead read = mapper.toRead(refEcg);

        assertThat(read.getRefEcg()).isEqualTo(refEcg.getRefEcg());
    }
}
