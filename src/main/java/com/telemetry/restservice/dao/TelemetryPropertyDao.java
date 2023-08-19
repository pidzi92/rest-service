package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelemetryPropertyDao extends CrudRepository<TelemetryProperty, Integer> {

    @Query("SELECT p FROM TelemetryProperty p")
    List<TelemetryProperty> findAllTelProps();
}
