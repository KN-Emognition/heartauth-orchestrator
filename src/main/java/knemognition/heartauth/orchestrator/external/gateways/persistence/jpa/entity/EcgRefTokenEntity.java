package knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ecg_ref_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EcgRefTokenEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "ecg_data", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<List<Float>> refEcg;

    @Column(name = "user_id", nullable = false)
    private UUID userId;   // <-- Just a column, no User import
}