package com.telemetry.restservice.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class Filter implements Serializable {
private String field;
private FilterOperationEnum operation = FilterOperationEnum.Equals;
private Object value;

}
