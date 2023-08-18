package com.telemetry.restservice.impl;

import com.telemetry.restservice.dao.TelemetryItemDao;
import com.telemetry.restservice.dao.VehicleDao;
import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.entity.Vehicle;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import com.telemetry.restservice.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {
    private TelemetryItemDao telemetryItemDao;
    private VehicleDao vehicleDao;

    @Autowired
    public TelemetryServiceImpl(TelemetryItemDao telemetryItemDao, VehicleDao vehicleDao){
        this.telemetryItemDao = telemetryItemDao;
        this.vehicleDao = vehicleDao;
    }

    @Override
    public List<TelemetryItem> fetchTelemetry() {
        Vehicle vehicleOne = Vehicle.builder().vehicleSn("SN001").build();
        Vehicle vehicleTwo = Vehicle.builder().vehicleSn("SN002").build();
        vehicleDao.save(vehicleOne);
        vehicleDao.save(vehicleTwo);


        TelemetryProperty firstVehicleFirstProp = TelemetryProperty.builder().telPropName("speed").telPropValue("100").telPropType(TelemetryPropertyTypeEnum.FLOAT).build();
        TelemetryProperty firstVehicleSecondProp = TelemetryProperty.builder().telPropName("fuel").telPropValue("200").telPropType(TelemetryPropertyTypeEnum.FLOAT).build();
        List<TelemetryProperty> firstVehicleProps = new ArrayList<>();
        firstVehicleProps.add(firstVehicleFirstProp);
        firstVehicleProps.add(firstVehicleSecondProp);

        TelemetryProperty secondVehicleFirstProp = TelemetryProperty.builder().telPropName("speed").telPropValue("300").telPropType(TelemetryPropertyTypeEnum.FLOAT).build();
        TelemetryProperty secondVehicleSecondProp = TelemetryProperty.builder().telPropName("fuel").telPropValue("400").telPropType(TelemetryPropertyTypeEnum.FLOAT).build();
        List<TelemetryProperty> secondVehicleProps = new ArrayList<>();
        secondVehicleProps.add(secondVehicleFirstProp);
        secondVehicleProps.add(secondVehicleSecondProp);

        TelemetryItem firstTel = TelemetryItem.builder().telProps(firstVehicleProps).vehicle(vehicleOne).build();
        TelemetryItem secondTel = TelemetryItem.builder().telProps(secondVehicleProps).vehicle(vehicleTwo).build();

        telemetryItemDao.save(firstTel);
        telemetryItemDao.save(secondTel);

        return telemetryItemDao.findAllTelItems();
    }
}
