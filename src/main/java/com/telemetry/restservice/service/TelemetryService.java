package com.telemetry.restservice.service;

import com.telemetry.restservice.entity.Vehicle;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TelemetryService {
    public List<Vehicle> fetchVehicles();
}
