package com.telemetry.restservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

/**
 * Enumeration representing different filter operations for telemetry items.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilterOperationEnum implements Serializable {
    Equals(),
    LessThan(),
    GreaterThan(),
    Contains();
}
