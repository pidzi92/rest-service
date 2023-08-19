package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.service.TelemetryService;
import com.telemetry.restservice.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TelemetryController {
    private TelemetryService telemetryService;
    private CsvUtil csvUtil;

    @Autowired
    public TelemetryController(TelemetryService telemetryService, CsvUtil csvUtil){
        this.telemetryService=telemetryService;
        this.csvUtil = csvUtil;
    }

    @GetMapping("query")
    public List<TelemetryItem> query(){
        return telemetryService.fetchTelemetry();
    }

    @GetMapping("importCsv")
    public void importCsv(){
        csvUtil.csvToDb();
    }
}
