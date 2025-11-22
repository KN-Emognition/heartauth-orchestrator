package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class GetUserDeviceHandlerTest {

    @Mock
    private TenantsModule tenantsModule;
    @Mock
    private UserMapper userMapper;
    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private GetUserDeviceHandler handler;

    @Test
    void shouldReturnMappedDevices() {
        UUID tenantId = UUID.randomUUID();
        UUID tenantInternalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        var cmd = IdentifiableUserCmd.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();
        var tenant = TenantRead.builder()
                .tenantId(tenantId)
                .id(tenantInternalId)
                .build();
        var entity = new DeviceEntity();
        var deviceRead = DeviceRead.builder()
                .deviceId("device")
                .build();
        when(tenantsModule.get(tenantId)).thenReturn(Optional.of(tenant));
        when(deviceRepository.findAllByAppUser_UserIdAndAppUser_TenantId(userId, tenantInternalId))
                .thenReturn(List.of(entity));
        when(userMapper.toReadModel(entity)).thenReturn(deviceRead);

        List<DeviceRead> result = handler.handle(cmd);

        assertThat(result).containsExactly(deviceRead);
        verify(deviceRepository).findAllByAppUser_UserIdAndAppUser_TenantId(userId, tenantInternalId);
    }
}
