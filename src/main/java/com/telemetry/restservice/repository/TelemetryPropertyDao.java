package com.telemetry.restservice.repository;

import com.telemetry.restservice.entity.TelemetryProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for interacting with TelemetryProperty entities in the database.
 */
@Repository
public interface TelemetryPropertyDao extends CrudRepository<TelemetryProperty, Integer> {
}
