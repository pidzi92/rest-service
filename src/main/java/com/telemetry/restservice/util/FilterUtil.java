package com.telemetry.restservice.util;

import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.FilterOperationEnum;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for validating and processing filters to be applied on telemetry items.
 */
@Slf4j
@Component
public class FilterUtil {
    private final String INVALID_FILTER_OMITTED_ERROR_LOG = "Invalid filter, omitted: {}";
    private final ColumnUtil columnUtil;

    /**
     * Constructor for the FilterUtil class. This class provides utility methods for filtering data.
     *
     * @param columnUtil An instance of the ColumnUtil class used for column-related operations.
     */
    @Autowired
    public FilterUtil(ColumnUtil columnUtil) {
        this.columnUtil = columnUtil;
    }

    /**
     * Validates filters by column type and value.
     * Invalid filters will be omitted from search criteria.
     *
     * This method allows specific filter operations only on specific columns and validates values according to column types.
     *
     * @param initialFilters Original list of filters.
     * @return List of valid Filter objects.
     */
    public List<Filter> validateFilters(List<Filter> initialFilters){
        List<Filter> validFilters = new ArrayList<>();

        for (Filter filter: initialFilters) {
            TelemetryPropertyTypeEnum columnType = columnUtil.getColumnType(filter.getField());
            if (!isValidOperation(columnType, filter.getOperation())) {
                log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                continue;
            }
            Object value = filter.getValue();
            validateValue(filter, columnType, value, validFilters);
        }
        return validFilters;
    }

    /**
     * Validates the value of a filter based on the column type and the filter to the list of valid filters.
     *
     * @param filter Filter object.
     * @param columnType Column type of the filter.
     * @param value Value of the filter.
     * @param validFilters List of valid filters.
     */
    private void validateValue(Filter filter, TelemetryPropertyTypeEnum columnType, Object value, List<Filter> validFilters) {
        switch(columnType){
            case DOUBLE:
               if (value instanceof Double){
                   validFilters.add(filter);
               }else{
                   log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                   return;
               }
                break;
            case INTEGER:
                if (value instanceof Integer){
                    validFilters.add(filter);
                }else{
                    log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                    return;
                }
                break;
            case STRING:
                if (value instanceof String){
                    validFilters.add(filter);
                }else{
                    log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                    return;
                }
                break;
            case DATETIME:
                try {
                    // convert to ms representation for easier comparison
                    Date dateValue = columnUtil.dateFormat.parse(value.toString());
                    filter.setValue(dateValue.toInstant().toEpochMilli());
                    validFilters.add(filter);
                }catch (ParseException e){
                    log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                    return;
                }
                break;
            case BOOLEAN:
                if (value instanceof Boolean){
                    filter.setValue((Boolean) value ? 1 : 0);
                    validFilters.add(filter);
                }else{
                    if (value instanceof String){

                        String lowerCaseValue = ((String) value).toLowerCase();

                        filter.setValue(String.valueOf(lowerCaseValue.equals("yes")
                                || lowerCaseValue.equals("active")
                                || lowerCaseValue.equals("on")
                                || lowerCaseValue.equals("true")
                                || lowerCaseValue.equals("1") ? 1 : 0));

                        validFilters.add(filter);
                    }else{
                        log.error(INVALID_FILTER_OMITTED_ERROR_LOG, filter);
                        return;
                    }
                }
                break;
        }
    }

    /**
     * Checks if an operation is valid for a specific column type.
     *
     * @param columnType TelemetryPropertyTypeEnum specifying the type of the column.
     * @param operation FilterOperationEnum specifying the desired operation.
     * @return True if the operation is valid on the specified column type, otherwise false.
     */
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
