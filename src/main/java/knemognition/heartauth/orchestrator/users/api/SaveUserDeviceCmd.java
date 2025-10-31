package knemognition.heartauth.orchestrator.users.api;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SaveUserDeviceCmd {
    DeviceCreate device;
    IdentifiableUserCmd user;
}
