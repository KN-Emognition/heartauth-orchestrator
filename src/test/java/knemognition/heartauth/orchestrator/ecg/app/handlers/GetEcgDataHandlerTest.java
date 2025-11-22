package knemognition.heartauth.orchestrator.ecg.app.handlers;

import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.app.mappers.EcgMapper;
import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEcgDataHandlerTest {

    @Mock
    private EcgStore ecgStore;
    @Mock
    private EcgMapper ecgMapper;

    @InjectMocks
    private GetEcgDataHandler handler;

    @Test
    void shouldReturnReferenceDataWhenPresent() {
        UUID userId = UUID.fromString("f290894f-8ea7-4299-9ea9-f07959c4f973");
        var cmd = GetRefDataCmd.builder()
                .userId(userId)
                .build();
        var refEcg = RefEcg.builder()
                .refEcg(List.of(List.of(0.42f)))
                .build();
        var read = RefEcgRead.builder()
                .refEcg(refEcg.getRefEcg())
                .build();
        when(ecgStore.getReferenceEcg(userId)).thenReturn(Optional.of(refEcg));
        when(ecgMapper.toRead(refEcg)).thenReturn(read);

        RefEcgRead result = handler.handle(cmd);

        assertThat(result).isEqualTo(read);
        verify(ecgStore).getReferenceEcg(userId);
    }

    @Test
    void shouldThrowWhenReferenceDataMissing() {
        UUID userId = UUID.fromString("b0ba0c28-2e33-4150-bc31-f46a9db840da");
        var cmd = GetRefDataCmd.builder()
                .userId(userId)
                .build();
        when(ecgStore.getReferenceEcg(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(NoSuchElementException.class);
    }
}
