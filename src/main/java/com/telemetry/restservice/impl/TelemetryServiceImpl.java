package com.telemetry.restservice.impl;

import com.telemetry.restservice.dao.TelemetryItemDao;
import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.service.TelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetryServiceImpl implements TelemetryService {
    private TelemetryItemDao telemetryItemDao;

    @Autowired
    public TelemetryServiceImpl(TelemetryItemDao telemetryItemDao){
        this.telemetryItemDao = telemetryItemDao;
    }

    @Override
    public List<TelemetryItem> fetchTelemetry() {
        return telemetryItemDao.findAllTelItems();
    }
}
