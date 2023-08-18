package com.telemetry.restservice.service;

import com.telemetry.restservice.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    List<Vehicle> fetchVehicles();
}
