package com.telemetry.restservice.impl;

import com.telemetry.restservice.dao.VehicleDao;
import com.telemetry.restservice.entity.Vehicle;
import com.telemetry.restservice.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {

    private VehicleDao vehicleDao;

    @Autowired
    public TelemetryServiceImpl(VehicleDao vehicleDao){
        this.vehicleDao = vehicleDao;
    }

    @Override
    public List<Vehicle> fetchVehicles() {
        Vehicle vehicleOne = Vehicle.builder().id(1L).sn("first vehicle").build();
        Vehicle vehicleTwo = Vehicle.builder().id(2L).sn("second vehicle").build();
        vehicleDao.save(vehicleOne);
        vehicleDao.save(vehicleTwo);
        return vehicleDao.findAllVehicles();
    }
}
