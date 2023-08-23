package com.telemetry.restservice.impl;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.TelemetryItemDTO;
import com.telemetry.restservice.service.TelemetryItemService;
import com.telemetry.restservice.util.ColumnUtil;
import com.telemetry.restservice.util.FilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the TelemetryItemService interface providing methods for filtering telemetry items.
 */
@Slf4j
@Service
public class TelemetryItemServiceImpl implements TelemetryItemService {
    private static final String TEL_PROP_VALUE = "telPropValue";
    private static final String TEL_PROPS = "telProps";
    private static final String TEL_PROP_NAME = "telPropName";
    private final EntityManager entityManager;

    private final FilterUtil filterUtil;

    private final ColumnUtil columnUtil;

    /**
     * Constructor for the TelemetryItemServiceImpl class. This class provides an implementation
     * of the TelemetryItemService interface, offering methods for working with telemetry items.
     *
     * @param entityManager The JPA EntityManager used for database interactions.
     * @param filterUtil An instance of the FilterUtil class used for filtering data.
     * @param columnUtil An instance of the ColumnUtil class used for column-related operations.
     */
    @Autowired
    public TelemetryItemServiceImpl(
            EntityManager entityManager, FilterUtil filterUtil,  ColumnUtil columnUtil) {
        this.entityManager = entityManager;
        this.filterUtil = filterUtil;
        this.columnUtil = columnUtil;
    }

    /**
     * Filters telemetry items based on the provided filters.
     *
     * @param filters List of Filter objects representing filters to apply on telemetry items.
     * @return A list of TelemetryItemDTO objects matching the applied filters.
     */
    public List<TelemetryItemDTO> filterTelemetryItems(List<Filter> filters) {
        List<Filter> validFilters =filterUtil.validateFilters(filters);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TelemetryItem> query = criteriaBuilder.createQuery(TelemetryItem.class);
        Root<TelemetryItem> telemetryItemRoot = query.from(TelemetryItem.class);

        List<Predicate> predicates = new ArrayList<>();
        for (Filter filter : validFilters) {
            Join<TelemetryItem, TelemetryProperty> telemetryPropertyJoin =
                    telemetryItemRoot.join(TEL_PROPS);

            Predicate propertyPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(
                            telemetryPropertyJoin.get(TEL_PROP_NAME),
                            filter.getField()), getPropValueFilter(filter, criteriaBuilder, telemetryPropertyJoin)
            );

            predicates.add(propertyPredicate);
        }

        Predicate combinedPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        query.where(combinedPredicate);

        List<TelemetryItem> telemetryItemList = entityManager.createQuery(query).getResultList();

        List<TelemetryItemDTO> dtoList = new ArrayList<>();
        for (TelemetryItem telemetryItem : telemetryItemList) {
            TelemetryItemDTO dto = convertToDTO(telemetryItem);
            dtoList.add(dto);
        }


        return dtoList;
    }

    private Predicate getPropValueFilter(Filter filter, CriteriaBuilder criteriaBuilder, Join<TelemetryItem, TelemetryProperty> telemetryPropertyJoin) {
        switch (filter.getOperation()) {
            case Equals:
                return criteriaBuilder.equal(telemetryPropertyJoin.get(TEL_PROP_VALUE), filter.getValue().toString());
            case LessThan:
                switch (columnUtil.getColumnType(filter.getField())){
                    case DOUBLE:
                        return criteriaBuilder.lessThan(telemetryPropertyJoin.get(TEL_PROP_VALUE), (Double) filter.getValue());
                    case INTEGER:
                    case DATETIME:
                        return criteriaBuilder.lessThan(telemetryPropertyJoin.get(TEL_PROP_VALUE), (Long) filter.getValue());
                }
            case GreaterThan:
                switch (columnUtil.getColumnType(filter.getField())) {
                    case DOUBLE:
                        return criteriaBuilder.greaterThan(telemetryPropertyJoin.get(TEL_PROP_VALUE), (Double) filter.getValue());
                    case INTEGER:
                    case DATETIME:
                        return criteriaBuilder.greaterThan(telemetryPropertyJoin.get(TEL_PROP_VALUE), (Long) filter.getValue());
                }
            case Contains:
                return criteriaBuilder.like(telemetryPropertyJoin.get(TEL_PROP_VALUE), "%" + filter.getValue() + "%");
            default:
                throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
        }
    }

    private TelemetryItemDTO convertToDTO(TelemetryItem telemetryItem) {
        TelemetryItemDTO dto = new TelemetryItemDTO();
        Map<String, String> propertyDTOs = new HashMap<>();
        for (TelemetryProperty prop:telemetryItem.getTelProps()) {

            String parsedValue;
            switch (prop.getTelPropType()){
                case BOOLEAN:
                  parsedValue = prop.getTelPropValue().equals("1") ? "Yes" : "No";
                    break;
                case  DATETIME:
                    Date date = new Date(Long.parseLong(prop.getTelPropValue()));
                    String formattedDateTime = columnUtil.dateFormat.format(date);
                    parsedValue = formattedDateTime;
                    break;
                default:
                   parsedValue = prop.getTelPropValue();
            }
            propertyDTOs.put(prop.getTelPropName(), parsedValue);
        }
        dto.setTelProps(propertyDTOs);
        return dto;
    }
}
