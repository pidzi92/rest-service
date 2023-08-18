package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelemetryItemDao extends CrudRepository<TelemetryItem, Integer> {
    @Query("SELECT i FROM TelemetryItem i")
    List<TelemetryItem> findAllTelItems();
}
