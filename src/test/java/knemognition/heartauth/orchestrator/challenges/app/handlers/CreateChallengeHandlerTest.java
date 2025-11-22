package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallengeRead;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.app.ports.PushSender;
import knemognition.heartauth.orchestrator.challenges.config.ChallengeProperties;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengePushMessage;
import knemognition.heartauth.orchestrator.challenges.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.users.api.DeviceRead;
import knemognition.heartauth.orchestrator.users.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.users.api.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.users.api.Platform;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateChallengeHandlerTest {

    private static final UUID USER_ID = UUID.fromString("a6c43e23-1aa4-4a14-a230-68c6faf3fc55");
    private static final UUID TENANT_ID = UUID.fromString("5aebd2c7-7a1e-496f-9a28-a59c9006b8d5");
    private static final UUID CHALLENGE_ID = UUID.fromString("1d784e4f-3a8d-4dac-a498-7dd5cb1f404c");

    @Mock
    private ChallengesMapper mapper;
    @Mock
    private SecurityModule securityModule;
    @Mock
    private UserModule userModule;
    @Mock
    private ChallengeProperties challengeProperties;
    @Mock
    private PushSender pushSender;
    @Mock
    private Environment environment;
    @Mock
    private ChallengeStore challengeStore;

    @InjectMocks
    private CreateChallengeHandler handler;

    @BeforeEach
    void setUp() {
        lenient().when(challengeProperties.getMinTtl()).thenReturn(30);
        lenient().when(challengeProperties.getMaxTtl()).thenReturn(90);
        lenient().when(challengeProperties.getDefaultTtl()).thenReturn(45);
        lenient().when(challengeProperties.getNonceLength()).thenReturn(32);
        lenient().when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});
    }

    @Test
    void shouldCreateChallengeAndSendPushToAllDevices() {
        var cmd = CreateChallengeCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .ttlSeconds(5)
                .build();
        var identifiableUser = IdentifiableUserCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        var firstDevice = device("device-1", "pub-1", "token-1");
        var secondDevice = device("device-2", "pub-2", "token-2");
        var createChallenge = CreateChallenge.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .ttlSeconds(30L)
                .nonceB64("nonce-from-handler")
                .userPublicKey(firstDevice.getPublicKey())
                .ephemeralPrivateKey("pem-private")
                .build();
        var createdResult = CreatedChallengeResult.builder()
                .id(CHALLENGE_ID)
                .exp(123L)
                .ttlSeconds(30L)
                .build();
        var pushMessage = ChallengePushMessage.builder()
                .challengeId(CHALLENGE_ID)
                .ttl(createdResult.getTtlSeconds())
                .nonce("nonce-from-handler")
                .publicKey("public-pem")
                .build();
        var read = CreatedChallengeRead.builder()
                .challengeId(CHALLENGE_ID)
                .build();
        var publicKey = mock(PublicKey.class);
        var privateKey = mock(PrivateKey.class);
        when(mapper.toCmd(cmd)).thenReturn(identifiableUser);
        when(userModule.checkIfUserExists(identifiableUser)).thenReturn(true);
        when(userModule.getUserDevices(identifiableUser)).thenReturn(List.of(firstDevice, secondDevice));
        when(securityModule.createNonce(32)).thenReturn("nonce-from-handler");
        when(securityModule.createEphemeralKeyPair()).thenReturn(new KeyPair(publicKey, privateKey));
        when(mapper.toDomain(eq(cmd), eq("nonce-from-handler"), eq(30), eq(privateKey), eq(firstDevice.getPublicKey()))).thenReturn(createChallenge);
        when(challengeStore.createChallenge(createChallenge)).thenReturn(createdResult);
        when(mapper.toChallengePushMessage(createdResult, "nonce-from-handler", publicKey)).thenReturn(pushMessage);
        when(mapper.toRead(createdResult)).thenReturn(read);

        var result = handler.handle(cmd);

        assertThat(result).isEqualTo(read);
        verify(pushSender).sendData(firstDevice.getFcmToken(), pushMessage);
        verify(pushSender).sendData(secondDevice.getFcmToken(), pushMessage);
        verify(challengeStore).createChallenge(createChallenge);
    }

    @Test
    void shouldUseUserIdAsNonceInE2eProfile() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev", SpringProfiles.E2E});
        var cmd = CreateChallengeCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .ttlSeconds(50)
                .build();
        var identifiableUser = IdentifiableUserCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        var device = device("device-1", "pub-1", "token-1");
        var publicKey = mock(PublicKey.class);
        var privateKey = mock(PrivateKey.class);
        var createChallenge = CreateChallenge.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .ttlSeconds(50L)
                .nonceB64(USER_ID.toString())
                .userPublicKey(device.getPublicKey())
                .ephemeralPrivateKey("pem-private")
                .build();
        var createdResult = CreatedChallengeResult.builder()
                .id(CHALLENGE_ID)
                .exp(321L)
                .ttlSeconds(50L)
                .build();
        when(mapper.toCmd(cmd)).thenReturn(identifiableUser);
        when(userModule.checkIfUserExists(identifiableUser)).thenReturn(true);
        when(userModule.getUserDevices(identifiableUser)).thenReturn(List.of(device));
        when(securityModule.createNonce(32)).thenReturn("random-nonce");
        when(securityModule.createEphemeralKeyPair()).thenReturn(new KeyPair(publicKey, privateKey));
        when(mapper.toDomain(eq(cmd), eq(USER_ID.toString()), eq(50), eq(privateKey), eq(device.getPublicKey()))).thenReturn(createChallenge);
        when(challengeStore.createChallenge(createChallenge)).thenReturn(createdResult);
        when(mapper.toChallengePushMessage(createdResult, USER_ID.toString(), publicKey)).thenReturn(ChallengePushMessage.builder()
                .challengeId(CHALLENGE_ID)
                .nonce(USER_ID.toString())
                .publicKey("public-pem")
                .ttl(50L)
                .build());
        when(mapper.toRead(createdResult)).thenReturn(CreatedChallengeRead.builder()
                .challengeId(CHALLENGE_ID)
                .build());

        var result = handler.handle(cmd);

        assertThat(result.getChallengeId()).isEqualTo(CHALLENGE_ID);
        verify(securityModule).createNonce(32);
        verify(mapper).toDomain(eq(cmd), eq(USER_ID.toString()), eq(50), eq(privateKey), eq(device.getPublicKey()));
    }

    @Test
    void shouldThrowWhenUserHasNoActiveDevices() {
        var cmd = CreateChallengeCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .ttlSeconds(40)
                .build();
        var identifiableUser = IdentifiableUserCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        when(mapper.toCmd(cmd)).thenReturn(identifiableUser);
        when(userModule.checkIfUserExists(identifiableUser)).thenReturn(true);
        when(userModule.getUserDevices(identifiableUser)).thenReturn(List.of());

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(NoActiveDeviceException.class);
        verify(pushSender, never()).sendData(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldThrowWhenUserDoesNotExist() {
        var cmd = CreateChallengeCmd.builder()
                .userId(USER_ID)
                .tenantId(TENANT_ID)
                .ttlSeconds(40)
                .build();
        var identifiableUser = IdentifiableUserCmd.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        when(mapper.toCmd(cmd)).thenReturn(identifiableUser);
        when(userModule.checkIfUserExists(identifiableUser)).thenReturn(false);

        assertThatThrownBy(() -> handler.handle(cmd))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(USER_ID.toString());
    }

    private static DeviceRead device(String id, String publicKey, String token) {
        return DeviceRead.builder()
                .deviceId(id)
                .displayName(id)
                .publicKey(publicKey)
                .fcmToken(token)
                .platform(Platform.ANDROID)
                .osVersion("1.0")
                .model("Pixel")
                .build();
    }
}
