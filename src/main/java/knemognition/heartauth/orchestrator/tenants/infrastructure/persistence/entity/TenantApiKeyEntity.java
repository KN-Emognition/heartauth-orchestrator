package knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tenant_api_keys", uniqueConstraints = @UniqueConstraint(name = "uq_key_per_tenant", columnNames = {"tenants_fk", "key_hash"}))
public class TenantApiKeyEntity {

    @Id
    @UuidGenerator
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
    @ToString.Include
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenants_fk", nullable = false, foreignKey = @ForeignKey(name = "tenant_api_keys_tenants_fk_fkey"))
    private TenantEntity tenant;

    @Column(name = "key_hash", nullable = false, columnDefinition = "text")
    private String keyHash;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}
