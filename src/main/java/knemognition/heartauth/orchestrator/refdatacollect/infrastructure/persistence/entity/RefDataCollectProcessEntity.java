package knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ref_data_collect")
public class RefDataCollectProcessEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "process_id", nullable = false)
    private UUID processId;

    @Column(name = "app_user_fk", nullable = false)
    private UUID userId;

    @Column(name = "last_notification_at", nullable = false)
    private OffsetDateTime lastNotificationAt;

    @Column(name = "interval", nullable = false)
    private Duration interval;

    @Column(name = "send_notification", nullable = false)
    private Boolean sendNotification;

    @Column(name = "samples_to_be_collected", nullable = false)
    private Integer samplesToBeCollected;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;
}
