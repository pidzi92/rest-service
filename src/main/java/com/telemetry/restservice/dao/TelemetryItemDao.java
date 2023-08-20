package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryItemDao extends CrudRepository<TelemetryItem, Integer> {
}
