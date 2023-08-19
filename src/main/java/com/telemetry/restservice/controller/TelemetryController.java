package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.FilterOperationEnum;
import com.telemetry.restservice.service.TelemetryService;
import com.telemetry.restservice.util.CsvImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
public class TelemetryController {
    private TelemetryService telemetryService;
    private CsvImporter csvImporter;

    @Autowired
    public TelemetryController(TelemetryService telemetryService, CsvImporter csvImporter){
        this.telemetryService=telemetryService;
        this.csvImporter = csvImporter;
    }

    @GetMapping("query")
    public List<TelemetryItem> query(){
        return telemetryService.fetchTelemetry();
    }

    @GetMapping("importCsv")
    public String importCsv(){
        csvImporter.csvToDb();
        return "OK";
    }

    @PostMapping("filter")
    public String filter(@RequestBody List<Filter> filters){
        log.info(filters.toString());
        return "Nothing yet";
    }
}
