package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.Vehicle;
import com.telemetry.restservice.service.TelemetryService;
import org.h2.util.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TelemetryController {
    private TelemetryService telemetryService;

    @Autowired
    public TelemetryController(TelemetryService telemetryService){
        this.telemetryService=telemetryService;
    }

    @GetMapping("query")
    public List<TelemetryItem> query(){
        return telemetryService.fetchTelemetry();
    }
}
