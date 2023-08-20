package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelemetryPropertyDao extends CrudRepository<TelemetryProperty, Integer> {
}
