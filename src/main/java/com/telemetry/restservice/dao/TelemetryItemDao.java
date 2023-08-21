package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object (DAO) interface for interacting with TelemetryItem entities in the database.
 */
@Repository
public interface TelemetryItemDao extends CrudRepository<TelemetryItem, Integer> {
}
