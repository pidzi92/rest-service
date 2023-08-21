package com.telemetry.restservice.util;

import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.FilterOperationEnum;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FilterUtilTest {

    @Mock
    private ColumnUtil columnUtil;

    @InjectMocks
    private FilterUtil filterUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateFilters() {
        List<Filter> initialFilters = new ArrayList<>();
        initialFilters.add(new Filter("temperature", FilterOperationEnum.GreaterThan, 25.0));
        initialFilters.add(new Filter("status", FilterOperationEnum.Equals, "active"));
        initialFilters.add(new Filter("timestamp", FilterOperationEnum.Contains, "2023-08-21"));

        when(columnUtil.getColumnType("temperature")).thenReturn(TelemetryPropertyTypeEnum.DOUBLE);
        when(columnUtil.getColumnType("status")).thenReturn(TelemetryPropertyTypeEnum.STRING);
        when(columnUtil.getColumnType("timestamp")).thenReturn(TelemetryPropertyTypeEnum.DATETIME);

        List<Filter> validFilters = filterUtil.validateFilters(initialFilters);

        assertEquals(2, validFilters.size());
    }

    @Test
    void testInvalidOperation() {
        List<Filter> initialFilters = new ArrayList<>();
        initialFilters.add(new Filter("temperature", FilterOperationEnum.Contains, "25.0"));

        when(columnUtil.getColumnType("temperature")).thenReturn(TelemetryPropertyTypeEnum.DOUBLE);

        List<Filter> validFilters = filterUtil.validateFilters(initialFilters);

        assertEquals(0, validFilters.size());
    }

    @Test
    void testInvalidValue() {
        List<Filter> initialFilters = new ArrayList<>();
        initialFilters.add(new Filter("temperature", FilterOperationEnum.GreaterThan, "invalid"));

        when(columnUtil.getColumnType("temperature")).thenReturn(TelemetryPropertyTypeEnum.DOUBLE);

        List<Filter> validFilters = filterUtil.validateFilters(initialFilters);

        assertEquals(0, validFilters.size());
    }
}
