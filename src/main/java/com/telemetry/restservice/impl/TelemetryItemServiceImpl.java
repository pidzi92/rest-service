package com.telemetry.restservice.impl;

import com.telemetry.restservice.dao.TelemetryItemRepository;
import com.telemetry.restservice.entity.TelemetryItem;
import com.telemetry.restservice.entity.TelemetryProperty;
import com.telemetry.restservice.model.Filter;
import com.telemetry.restservice.service.TelemetryItemService;
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

@Service
public class TelemetryItemServiceImpl implements TelemetryItemService {

    private final TelemetryItemRepository telemetryItemRepository;
    private final EntityManager entityManager;

    @Autowired
    public TelemetryItemServiceImpl(
            TelemetryItemRepository telemetryItemRepository,
            EntityManager entityManager) {
        this.telemetryItemRepository = telemetryItemRepository;
        this.entityManager = entityManager;
    }

    public List<TelemetryItem> filterTelemetryItems(List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TelemetryItem> query = criteriaBuilder.createQuery(TelemetryItem.class);
        Root<TelemetryItem> telemetryItemRoot = query.from(TelemetryItem.class);

        List<Predicate> predicates = new ArrayList<>();
        for (Filter filter : filters) {
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

        return entityManager.createQuery(query).getResultList();
    }
    private static Predicate getPropValueFilter(Filter filter, CriteriaBuilder criteriaBuilder, Join<TelemetryItem, TelemetryProperty> telemetryPropertyJoin) {
        switch (filter.getOperation()) {
            case Equals:
                return criteriaBuilder.equal(telemetryPropertyJoin.get("telPropValue"), filter.getValue());
            case LessThan:
                return criteriaBuilder.lessThan(telemetryPropertyJoin.get("telPropValue"), (Double) filter.getValue());
            case GreaterThan:
                return criteriaBuilder.greaterThan(telemetryPropertyJoin.get("telPropValue"), (Double) filter.getValue());
            case Contains:
                return criteriaBuilder.like(telemetryPropertyJoin.get("telPropValue"), "%" + filter.getValue() + "%");
            default:
                throw new IllegalArgumentException("Unsupported operation: " + filter.getOperation());
        }
    }

}
