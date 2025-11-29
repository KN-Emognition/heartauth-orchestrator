package knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity.RefDataCollectNotificationRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RefDataCollectNotificationRepository extends CrudRepository<RefDataCollectNotificationRedis, UUID> {
}
