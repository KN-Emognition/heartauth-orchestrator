package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ecg_ref_data", uniqueConstraints = @UniqueConstraint(name = "uq_ecg_per_user", columnNames = "app_user_fk"))
public class EcgRefDataEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_fk", nullable = false, foreignKey = @ForeignKey(name = "ecg_ref_data_app_user_fk_fkey"))
    private UserEntity appUser;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ecg_data", nullable = false, columnDefinition = "jsonb")
    private List<List<Float>> refEcg;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}
