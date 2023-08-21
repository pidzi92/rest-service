package com.telemetry.restservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Model class representing a filter to be applied on telemetry items.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Filter implements Serializable {
private String field;
private FilterOperationEnum operation = FilterOperationEnum.Equals;
private Object value;
}
