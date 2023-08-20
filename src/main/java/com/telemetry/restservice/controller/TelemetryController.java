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

@Api(value = "TelemetryController")
@Slf4j
@RestController
public class TelemetryController {

    private CsvImporter csvImporter;
    private final TelemetryItemServiceImpl telemetryItemServiceImpl;

    @Autowired
    public TelemetryController(CsvImporter csvImporter, TelemetryItemServiceImpl telemetryItemServiceImpl){
        this.csvImporter = csvImporter;
        this.telemetryItemServiceImpl = telemetryItemServiceImpl;
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
     * @param filters - List of @{@link Filter} to apply on the telemetry items form DB
     *
     * @return filtered list of @{@link TelemetryItem} from DB
     */
    @PostMapping("filter")
    public List<TelemetryItem> filter(@RequestBody List<Filter> filters){
        return telemetryItemServiceImpl.filterTelemetryItems(filters);
    }
}
