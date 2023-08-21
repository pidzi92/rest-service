package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.impl.TelemetryItemServiceImpl;
import com.telemetry.restservice.model.TelemetryItemDTO;
import com.telemetry.restservice.service.CsvImporterService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class responsible for handling telemetry-related endpoints.
 */
@Api(value = "TelemetryController")
@Slf4j
@RestController
public class TelemetryController {

    private CsvImporterService csvImporterService;
    private final TelemetryItemServiceImpl telemetryItemServiceImpl;

    /**
     * Constructor for TelemetryController.
     *
     * @param csvImporterService              The CSV importer service instance for importing telemetry data.
     * @param telemetryItemServiceImpl The service implementation for telemetry items.
     */
    @Autowired
    public TelemetryController(CsvImporterService csvImporterService, TelemetryItemServiceImpl telemetryItemServiceImpl){
        this.csvImporterService = csvImporterService;
        this.telemetryItemServiceImpl = telemetryItemServiceImpl;
    }

    /**
     * Endpoint used for importing csv telemetry data into DB
     */
    @GetMapping("importCsv")
    public void importCsv(){
            csvImporterService.csvToDb();
    }

    /**
     * Filters telemetry items based on the provided filters.
     *
     * @param filters List of {@link Filter} objects representing filters to apply on telemetry items.
     * @return A filtered list of {@link TelemetryItem} objects from the database.
     */
    @PostMapping("filter")
    public List<TelemetryItemDTO> filter(@RequestBody List<Filter> filters){
        return telemetryItemServiceImpl.filterTelemetryItems(filters);
    }
}
