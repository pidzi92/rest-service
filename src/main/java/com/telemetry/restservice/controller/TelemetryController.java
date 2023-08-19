package com.telemetry.restservice.controller;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.FilterOperationEnum;
import com.telemetry.restservice.service.TelemetryService;
import com.telemetry.restservice.util.CsvImporter;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "TelemetryController")
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

    /**
     *
     * @return List of all t@{@link TelemetryItem} from DB
     */
    @GetMapping("query")
    public List<TelemetryItem> query(){
        return telemetryService.fetchTelemetry();
    }

    /**
     *
     * @return status message of the
     */
    @GetMapping("importCsv")
    public String importCsv(){
            csvImporter.csvToDb();
            return "OK";
    }

    /**
     *
     * @param filters - List of @{@link Filter} to apply on the telemetry items form DB
     * @return filtered list of @{@link TelemetryItem} from DB
     */
    @PostMapping("filter")
    public String filter(@RequestBody List<Filter> filters){
        log.info(filters.toString());
        return "Nothing yet";
    }
}
