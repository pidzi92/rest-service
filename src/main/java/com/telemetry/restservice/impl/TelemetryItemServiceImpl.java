package com.telemetry.restservice.impl;

import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.model.TelemetryItemDTO;
import com.telemetry.restservice.service.TelemetryItemService;
import com.telemetry.restservice.util.ColumnUtil;
import com.telemetry.restservice.util.FilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the TelemetryItemService interface providing methods for filtering telemetry items.
 */
@Service
public class TelemetryItemServiceImpl implements TelemetryItemService {
    private final EntityManager entityManager;

    private final FilterUtil filterUtil;

    private final ColumnUtil columnUtil;

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
     * @return A list of TelemetryItem objects matching the applied filters.
     */
    public List<TelemetryItemDTO> filterTelemetryItems(List<Filter> filters) {
        List<Filter> validFilters =filterUtil.validateFilters(filters);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TelemetryItem> query = criteriaBuilder.createQuery(TelemetryItem.class);
        Root<TelemetryItem> telemetryItemRoot = query.from(TelemetryItem.class);

        List<Predicate> predicates = new ArrayList<>();
        for (Filter filter : validFilters) {
            Join<TelemetryItem, TelemetryProperty> telemetryPropertyJoin =
                    telemetryItemRoot.join("telProps");

            Predicate propertyPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(
                            telemetryPropertyJoin.get("telPropName"),
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


    public TelemetryItemDTO convertToDTO(TelemetryItem telemetryItem) {
        TelemetryItemDTO dto = new TelemetryItemDTO();
        dto.setTelProps(telemetryItem.getTelProps());
        return dto;
    }
    private Predicate getPropValueFilter(Filter filter, CriteriaBuilder criteriaBuilder, Join<TelemetryItem, TelemetryProperty> telemetryPropertyJoin) {
        switch (filter.getOperation()) {
            case Equals:
                return criteriaBuilder.equal(telemetryPropertyJoin.get("telPropValue"), filter.getValue().toString());
            case LessThan:
                switch (columnUtil.getColumnType(filter.getField())){
                    case DOUBLE:
                        return criteriaBuilder.lessThan(telemetryPropertyJoin.get("telPropValue"), (Double) filter.getValue());
                    case INTEGER:
                    case DATETIME:
                        return criteriaBuilder.lessThan(telemetryPropertyJoin.get("telPropValue"), (Long) filter.getValue());
                }
            case GreaterThan:
                switch (columnUtil.getColumnType(filter.getField())) {
                    case DOUBLE:
                        return criteriaBuilder.greaterThan(telemetryPropertyJoin.get("telPropValue"), (Double) filter.getValue());
                    case INTEGER:
                    case DATETIME:
                        return criteriaBuilder.greaterThan(telemetryPropertyJoin.get("telPropValue"), (Long) filter.getValue());
                }
            case Contains:
                return criteriaBuilder.like(telemetryPropertyJoin.get("telPropValue"), "%" + filter.getValue() + "%");
            default:
                throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
        }
    }
}
