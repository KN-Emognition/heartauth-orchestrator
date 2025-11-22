package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SaveReferenceDataHandlerTest {

    @Mock
    private EcgStore ecgStore;

    @InjectMocks
    private SaveReferenceDataHandler handler;

    @Test
    void shouldPersistReferenceDataUsingStore() {
        var cmd = SaveReferenceDataCmd.builder()
                .userId(UUID.fromString("f0f4a1f1-36a7-4b8b-8e24-7e7bf11df999"))
                .refEcg(List.of(List.of(0.1f, 0.2f), List.of(0.3f)))
                .build();

        handler.handle(cmd);

        ArgumentCaptor<UUID> userCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<RefEcg> refCaptor = ArgumentCaptor.forClass(RefEcg.class);
        verify(ecgStore).saveReferenceEcg(userCaptor.capture(), refCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(cmd.getUserId());
        assertThat(refCaptor.getValue().getRefEcg()).isEqualTo(cmd.getRefEcg());
    }
}
