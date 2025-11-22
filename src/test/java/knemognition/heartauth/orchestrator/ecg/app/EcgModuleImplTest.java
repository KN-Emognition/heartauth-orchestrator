package knemognition.heartauth.orchestrator.ecg.app;

import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.ecg.api.RefEcgRead;
import knemognition.heartauth.orchestrator.ecg.api.SaveReferenceDataCmd;
import knemognition.heartauth.orchestrator.ecg.app.handlers.GetEcgDataHandler;
import knemognition.heartauth.orchestrator.ecg.app.handlers.SaveReferenceDataHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EcgModuleImplTest {

    @Mock
    private GetEcgDataHandler getHandler;
    @Mock
    private SaveReferenceDataHandler saveHandler;

    @InjectMocks
    private EcgModuleImpl module;

    @Test
    void shouldDelegateSaveReferenceData() {
        var cmd = SaveReferenceDataCmd.builder()
                .userId(UUID.randomUUID())
                .build();

        module.saveReferenceData(cmd);

        verify(saveHandler).handle(cmd);
    }

    @Test
    void shouldDelegateGetReferenceData() {
        var cmd = GetRefDataCmd.builder()
                .userId(UUID.randomUUID())
                .build();
        var read = RefEcgRead.builder()
                .build();
        when(getHandler.handle(cmd)).thenReturn(read);

        RefEcgRead result = module.getUserReferenceData(cmd);

        assertThat(result).isEqualTo(read);
        verify(getHandler).handle(cmd);
    }
}
