package com.telemetry.restservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelemetryController {

    @GetMapping("query")
    public String query(){
        return "Hello Everyone";
    }
}
