package com.telemetry.restservice.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TelemetryPropertyDTO {
    private String telPropName;
    private String telPropValue;
}
