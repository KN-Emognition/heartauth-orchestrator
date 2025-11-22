package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.UserRead;
import knemognition.heartauth.orchestrator.users.app.mappers.UserMapper;
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
class GetUserHandlerTest {

    @Mock
    private TenantsModule tenantsModule;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private GetUserHandler handler;

    @Test
    void shouldReturnMappedUserWhenFound() {
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
        var entity = new UserEntity();
        var read = UserRead.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();
        when(tenantsModule.get(tenantId)).thenReturn(Optional.of(tenant));
        when(userRepository.findByUserIdAndTenantId(userId, tenantInternalId)).thenReturn(Optional.of(entity));
        when(mapper.toReadModel(entity)).thenReturn(read);

        Optional<UserRead> result = handler.handle(cmd);

        assertThat(result).contains(read);
        verify(userRepository).findByUserIdAndTenantId(userId, tenantInternalId);
    }
}
