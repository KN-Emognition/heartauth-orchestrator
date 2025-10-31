//package knemognition.heartauth.orchestrator.internal.app.impl;
//
//import knemognition.heartauth.orchestrator.internal.app.mapper.ResponseStatusMapper;
//import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
//import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
//import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
//import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
//import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import test.config.HeartauthUnitTest;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class ChallengeStatusServiceImplTest extends HeartauthUnitTest {
//
//    @Mock
//    StatusStore<ChallengeState> store;
//    @Mock
//    ResponseStatusMapper mapper;
//    @InjectMocks
//    ChallengeStatusServiceImpl service;
//
//    @Test
//    void status_returns_notFound_when_absent() {
//        UUID id = UUID.randomUUID();
//        StatusResponse notFound = new StatusResponse(FlowStatus.NOT_FOUND);
//        when(store.getStatus(id)).thenReturn(Optional.empty());
//        when(mapper.notFound()).thenReturn(notFound);
//
//        StatusResponse result = service.status(id);
//
//        assertThat(result).isSameAs(notFound);
//        verify(store).getStatus(id);
//        verify(mapper).notFound();
//        verify(mapper, never()).map(any());
//    }
//
//    @Test
//    void setStatus_delegates_and_returns_boolean() {
//        StatusChange change = mock(StatusChange.class);
//        when(store.setStatus(change)).thenReturn(true);
//
//        boolean result = service.setStatus(change);
//
//        assertThat(result).isTrue();
//        ArgumentCaptor<StatusChange> captor = ArgumentCaptor.forClass(StatusChange.class);
//        verify(store).setStatus(captor.capture());
//        assertThat(captor.getValue()).isSameAs(change);
//    }
//}
