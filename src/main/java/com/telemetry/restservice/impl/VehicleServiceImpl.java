package com.telemetry.restservice.impl;

import com.telemetry.restservice.dao.VehicleDao;
import com.telemetry.restservice.entity.Vehicle;
import com.telemetry.restservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {


    @Override
    public List<Vehicle> fetchVehicles() {
        return null;
    }
}
