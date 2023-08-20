package com.telemetry.restservice.service;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;

import java.util.List;

public interface TelemetryItemService {
    List<TelemetryItem> filterTelemetryItems(List<Filter> filters);
}
