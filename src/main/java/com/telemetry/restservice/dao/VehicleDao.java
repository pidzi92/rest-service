package com.telemetry.restservice.dao;

import com.telemetry.restservice.entity.Vehicle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VehicleDao extends CrudRepository<Vehicle, Integer> {
    @Query("SELECT v FROM Vehicle v")
    List<Vehicle> findAllVehicles();
}
