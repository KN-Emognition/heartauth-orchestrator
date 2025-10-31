package knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(name = "uq_user_per_tenant", columnNames = {"tenants_fk", "user_id"}))
public class UserEntity {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    @ToString.Include
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DeviceEntity> devices = new HashSet<>();

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private EcgRefDataEntity ecgRefData;


    public void addDevice(DeviceEntity device) {
        if (device == null) return;
        device.setAppUser(this);
        this.devices.add(device);
    }

    public void replaceRefData(EcgRefDataEntity ref) {
        if (this.ecgRefData != null) this.ecgRefData.setAppUser(null);
        this.ecgRefData = ref;
        if (ref != null) ref.setAppUser(this);
    }
}
