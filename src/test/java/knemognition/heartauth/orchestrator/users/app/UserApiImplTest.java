package knemognition.heartauth.orchestrator.users.app;

import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.SaveUserDeviceCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.app.handlers.CheckIfUserExistsHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.GetUserDeviceHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.GetUserHandler;
import knemognition.heartauth.orchestrator.users.app.handlers.SaveUserDeviceHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserApiImplTest {

    @Mock
    private CheckIfUserExistsHandler checkHandler;
    @Mock
    private GetUserDeviceHandler deviceHandler;
    @Mock
    private GetUserHandler getUserHandler;
    @Mock
    private SaveUserDeviceHandler saveHandler;

    @InjectMocks
    private UserApiImpl userApi;

    @Test
    void shouldDelegateCheckIfUserExists() {
        var user = IdentifiableUserCmd.builder().build();
        when(checkHandler.handle(user)).thenReturn(true);

        assertThat(userApi.checkIfUserExists(user)).isTrue();
        verify(checkHandler).handle(user);
    }

    @Test
    void shouldDelegateGetUser() {
        var user = IdentifiableUserCmd.builder().build();
        var read = UserRead.builder().build();
        when(getUserHandler.handle(user)).thenReturn(Optional.of(read));

        assertThat(userApi.getUser(user)).contains(read);
    }

    @Test
    void shouldDelegateSaveUserDevice() {
        var cmd = SaveUserDeviceCmd.builder().build();
        var read = UserRead.builder().build();
        when(saveHandler.handle(cmd)).thenReturn(read);

        assertThat(userApi.saveUserDevice(cmd)).isEqualTo(read);
    }

    @Test
    void shouldDelegateGetUserDevices() {
        var user = IdentifiableUserCmd.builder().build();
        var devices = List.of(DeviceRead.builder().build());
        when(deviceHandler.handle(user)).thenReturn(devices);

        assertThat(userApi.getUserDevices(user)).isEqualTo(devices);
    }
}
