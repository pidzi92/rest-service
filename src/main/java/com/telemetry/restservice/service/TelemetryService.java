package com.telemetry.restservice.service;

import com.telemetry.restservice.entity.TelemetryItem;


import java.util.List;

public interface TelemetryService {
    List<TelemetryItem> fetchTelemetry();
}
