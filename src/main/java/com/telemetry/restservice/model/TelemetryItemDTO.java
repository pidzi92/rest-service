package com.telemetry.restservice.model;

import com.telemetry.restservice.entity.TelemetryProperty;
import java.util.List;

public class TelemetryItemDTO {
    private List<TelemetryProperty> telProps;

    public List<TelemetryProperty> getTelProps() {
        return telProps;
    }

    public void setTelProps(List<TelemetryProperty> telProps) {
        this.telProps = telProps;
    }
}