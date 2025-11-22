package knemognition.heartauth.orchestrator.users.app.mappers;

import knemognition.heartauth.orchestrator.users.api.DeviceCreate;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.Platform;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserEntityToRead() {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setUserId(userId);

        UserRead read = mapper.toReadModel(entity);

        assertThat(read.getTenantId()).isEqualTo(tenantId);
        assertThat(read.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldMapDeviceEntityToRead() {
        DeviceEntity entity = new DeviceEntity();
        entity.setDeviceId("device-1");
        entity.setDisplayName("My Device");
        entity.setPublicKey("pub");
        entity.setPlatform(Platform.IOS);
        entity.setModel("iPhone");
        entity.setOsVersion("17");
        entity.setFcmToken("token");

        DeviceRead read = mapper.toReadModel(entity);

        assertThat(read.getDeviceId()).isEqualTo("device-1");
        assertThat(read.getDisplayName()).isEqualTo("My Device");
        assertThat(read.getPlatform()).isEqualTo(Platform.IOS);
    }

    @Test
    void shouldMapCommandsToEntities() {
        IdentifiableUserCmd userCmd = IdentifiableUserCmd.builder()
                .tenantId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();
        DeviceCreate deviceCreate = DeviceCreate.builder()
                .deviceId("dev")
                .displayName("Device")
                .publicKey("pem")
                .platform(Platform.ANDROID)
                .model("Pixel")
                .build();

        UserEntity userEntity = mapper.toEntity(userCmd);
        DeviceEntity deviceEntity = mapper.toEntity(deviceCreate);

        assertThat(userEntity.getUserId()).isEqualTo(userCmd.getUserId());
        assertThat(deviceEntity.getDeviceId()).isEqualTo("dev");
        assertThat(deviceEntity.getPlatform()).isEqualTo(Platform.ANDROID);
    }
}
