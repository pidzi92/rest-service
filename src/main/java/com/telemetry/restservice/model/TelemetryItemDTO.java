package com.telemetry.restservice.model;

import lombok.Data;

import java.util.List;

@Data
public class TelemetryItemDTO {
    private List<TelemetryPropertyDTO> telProps;
}
