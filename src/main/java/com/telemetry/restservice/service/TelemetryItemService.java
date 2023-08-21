package com.telemetry.restservice.service;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.model.Filter;
import java.util.List;

/**
 * Service interface for filtering telemetry items based on specified filters.
 */

public interface TelemetryItemService {

    /**
     * Filters telemetry items based on the provided filters.
     *
     * @param filters List of Filter objects representing filters to apply on telemetry items.
     * @return A list of TelemetryItem objects matching the applied filters.
     */
    List<TelemetryItem> filterTelemetryItems(List<Filter> filters);
}
