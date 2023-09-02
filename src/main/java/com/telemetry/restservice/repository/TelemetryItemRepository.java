package com.telemetry.restservice.repository;

import com.telemetry.restservice.entity.TelemetryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing TelemetryItem entities in the database.
 */
@Repository
public interface TelemetryItemRepository extends JpaRepository<TelemetryItem, Long> {
}