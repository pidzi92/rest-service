package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.service.TelemetryItemService;
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

    private final CsvImporterService csvImporterService;
    private final TelemetryItemService telemetryItemService;

    /**
     * Constructor for TelemetryController.
     *
     * @param csvImporterService              The CSV importer service instance for importing telemetry data.
     * @param telemetryItemService The service instance for telemetry items.
     */
    @Autowired
    public TelemetryController(CsvImporterService csvImporterService, TelemetryItemService telemetryItemService){
        this.csvImporterService = csvImporterService;
        this.telemetryItemService = telemetryItemService;
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
        return telemetryItemService.filterTelemetryItems(filters);
    }
}
