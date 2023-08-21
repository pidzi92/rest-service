package com.telemetry.restservice.model;

import lombok.Data;
import java.util.Map;

@Data
public class TelemetryItemDTO {
    private Map<String, String> telProps;
}