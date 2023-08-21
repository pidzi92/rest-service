package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.impl.TelemetryItemServiceImpl;
import com.telemetry.restservice.util.CsvImporter;
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

    private CsvImporter csvImporter;
    private final TelemetryItemServiceImpl telemetryItemServiceImpl;

    /**
     * Constructor for TelemetryController.
     *
     * @param csvImporter              The CSV importer instance for importing telemetry data.
     * @param telemetryItemServiceImpl The service implementation for telemetry items.
     */
    @Autowired
    public TelemetryController(CsvImporter csvImporter, TelemetryItemServiceImpl telemetryItemServiceImpl){
        this.csvImporter = csvImporter;
        this.telemetryItemServiceImpl = telemetryItemServiceImpl;
    }

    /**
     * Endpoint used for importing csv telemetry data into DB
     */
    @GetMapping("importCsv")
    public void importCsv(){
            csvImporter.csvToDb();
    }

    /**
     * Filters telemetry items based on the provided filters.
     *
     * @param filters List of {@link Filter} objects representing filters to apply on telemetry items.
     * @return A filtered list of {@link TelemetryItem} objects from the database.
     */
    @PostMapping("filter")
    public List<TelemetryItem> filter(@RequestBody List<Filter> filters){
        return telemetryItemServiceImpl.filterTelemetryItems(filters);
    }
}
