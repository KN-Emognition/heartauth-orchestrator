package knemognition.heartauth.orchestrator.user.app;

import knemognition.heartauth.orchestrator.user.api.DeviceRead;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.user.api.UserApi;
import knemognition.heartauth.orchestrator.user.app.handlers.CheckIfUserExistsHandler;
import knemognition.heartauth.orchestrator.user.app.handlers.GetUserDeviceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApiImpl implements UserApi {

    private final CheckIfUserExistsHandler checkIfUserExistsHandler;
    private final GetUserDeviceHandler getUserDeviceHandler;

    @Override
    public boolean checkIfUserExists(IdentifiableUserCmd user) {
        return checkIfUserExistsHandler.handle(user);
    }

    @Override
    public List<DeviceRead> getUserDevices(IdentifiableUserCmd user) {
        return getUserDeviceHandler.handle(user);
    }
}
