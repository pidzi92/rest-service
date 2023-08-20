package com.telemetry.restservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FilterOperationEnum implements Serializable {
    Equals(1),
    LessThan(2),
    GreaterThan(3),
    Contains(4);

    private int operationCode;

    FilterOperationEnum(int operationCode) {
        this.operationCode =operationCode;
    }

    public int getOperationCode() {

        return this.operationCode;
    }
}
