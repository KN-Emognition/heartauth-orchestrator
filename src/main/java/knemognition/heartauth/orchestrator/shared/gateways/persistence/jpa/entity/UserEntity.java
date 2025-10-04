package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor()
@AllArgsConstructor
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenants_fk", nullable = false, foreignKey = @ForeignKey(name = "app_user_tenants_fk_fkey"))
    private TenantEntity tenant;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Builder.Default
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
