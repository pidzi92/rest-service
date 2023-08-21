package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing TelemetryItem entities in the database.
 */
@Repository
public interface TelemetryItemRepository extends JpaRepository<TelemetryItem, Long> {
}