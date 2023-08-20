package com.telemetry.restservice.util;

import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.FilterOperationEnum;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class FilterUtil {
    @Autowired ColumnUtil columnUtil;

    public List<Filter> validateFilter(List<Filter> initialFilters){
        List<Filter> validFilters = new ArrayList<>();

        for (Filter filter: initialFilters) {
            TelemetryPropertyTypeEnum columnType = columnUtil.getColumnType(filter.getField());
            if (!isValidOperation(columnType, filter.getOperation())) {
                log.error("Invalid filter, omitted: {}", filter);
                continue;
            }
            Object value = filter.getValue();
            switch(columnType){
                case DOUBLE:
                   if (value instanceof Double){
                       validFilters.add(filter);
                   }else{
                       log.error("Invalid filter, omitted: {}", filter);
                       continue;
                   }
                    break;
                case INTEGER:
                    if (value instanceof  Integer){
                        validFilters.add(filter);
                    }else{
                        log.error("Invalid filter, omitted: {}", filter);
                        continue;
                    }
                    break;
                case STRING:
                    if (value instanceof  String){
                        validFilters.add(filter);
                    }else{
                        log.error("Invalid filter, omitted: {}", filter);
                        continue;
                    }
                    break;
                case DATETIME:
                    try {
                        Date dateValue = columnUtil.dateFormat.parse(value.toString());
                        filter.setValue(dateValue.toInstant().toEpochMilli());
                        validFilters.add(filter);
                    }catch (ParseException e){
                        log.error("Invalid filter, omitted: {}", filter);
                        continue;
                    }
                    break;
                case BOOLEAN:
                    if (value instanceof  Boolean){
                        validFilters.add(filter);
                    }else{
                        log.error("Invalid filter, omitted: {}", filter);
                        continue;
                    }
                    break;
            }
        }
        return validFilters;
    }

    private boolean isValidOperation(TelemetryPropertyTypeEnum columnType, FilterOperationEnum operation){
        switch (operation){
            case Contains:
                return columnType == TelemetryPropertyTypeEnum.STRING;
            case GreaterThan:
            case LessThan:
                return columnType == TelemetryPropertyTypeEnum.DOUBLE
                        || columnType == TelemetryPropertyTypeEnum.INTEGER
                        || columnType == TelemetryPropertyTypeEnum.DATETIME;
            case Equals:
                return true;
            default:
                return false;
        }
    }
}
