package knemognition.heartauth.orchestrator.users.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
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
class CheckIfUserExistsHandlerTest {

    @Mock
    private TenantsModule tenantsModule;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CheckIfUserExistsHandler handler;

    @Test
    void shouldQueryRepositoryWithTenantInternalId() {
        UUID tenantId = UUID.fromString("4f89bb4e-67b7-4ef7-95d8-681a91cf9ad4");
        UUID tenantInternalId = UUID.fromString("88891d29-e16f-44a2-81bd-e7d48cb3af11");
        UUID userId = UUID.fromString("6ea6d9c0-2a56-4d42-9b3f-d0af6bc0ae0a");
        var cmd = IdentifiableUserCmd.builder()
                .tenantId(tenantId)
                .userId(userId)
                .build();
        var tenantRead = TenantRead.builder()
                .tenantId(tenantId)
                .id(tenantInternalId)
                .build();
        when(tenantsModule.get(tenantId)).thenReturn(Optional.of(tenantRead));
        when(userRepository.existsByUserIdAndTenantId(userId, tenantInternalId)).thenReturn(true);

        boolean exists = handler.handle(cmd);

        assertThat(exists).isTrue();
        verify(userRepository).existsByUserIdAndTenantId(userId, tenantInternalId);
    }
}
