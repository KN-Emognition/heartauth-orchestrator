package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.SaveUserDeviceCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.UserEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveUserDeviceHandlerTest {

    @Mock
    private TenantsModule tenantsModule;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private SaveUserDeviceHandler handler;

    @Test
    void shouldPersistUserAndDeviceWithTenantContext() {
        UUID tenantId = UUID.randomUUID();
        UUID tenantInternalId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        var userCmd = IdentifiableUserCmd.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();
        var deviceCreate = DeviceCreate.builder()
                .deviceId("device-1")
                .platform(knemognition.heartauth.orchestrator.users.api.Platform.ANDROID)
                .build();
        var cmd = SaveUserDeviceCmd.builder()
                .user(userCmd)
                .device(deviceCreate)
                .build();
        var tenant = TenantRead.builder()
                .tenantId(tenantId)
                .id(tenantInternalId)
                .build();
        var userEntity = new UserEntity();
        var deviceEntity = new DeviceEntity();
        var readModel = UserRead.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();
        when(tenantsModule.get(tenantId)).thenReturn(Optional.of(tenant));
        when(userMapper.toEntity(userCmd)).thenReturn(userEntity);
        when(userMapper.toEntity(deviceCreate)).thenReturn(deviceEntity);
        when(userMapper.toReadModel(userEntity)).thenReturn(readModel);

        UserRead result = handler.handle(cmd);

        assertThat(result).isEqualTo(readModel);
        assertThat(userEntity.getTenantId()).isEqualTo(tenantInternalId);
        assertThat(deviceEntity.getAppUser()).isEqualTo(userEntity);
        verify(userRepository).save(userEntity);
    }
}
