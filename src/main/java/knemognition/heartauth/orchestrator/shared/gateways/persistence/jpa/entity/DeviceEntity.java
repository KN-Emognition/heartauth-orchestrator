package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity;

import jakarta.persistence.*;
import knemognition.heartauth.orchestrator.external.model.Platform;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_device", uniqueConstraints = @UniqueConstraint(name = "uq_device_per_user", columnNames = {"app_user_fk", "device_id"}))
public class DeviceEntity {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    @ToString.Include
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_fk", nullable = false, foreignKey = @ForeignKey(name = "user_device_app_user_fk_fkey"))
    @ToString.Exclude
    private UserEntity appUser;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "public_key_pem", nullable = false, columnDefinition = "text")
    private String publicKeyPem;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, columnDefinition = "platform_enum")
    private Platform platform;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "model")
    private String model;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "last_seen_at")
    private OffsetDateTime lastSeenAt;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;
}
