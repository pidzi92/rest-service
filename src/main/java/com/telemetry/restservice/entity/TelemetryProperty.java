package com.telemetry.restservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telemetry.restservice.model.TelemetryPropertyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity class representing telemetry properties associated with telemetry items in the database.
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryProperty implements Serializable {
    /**
     * Primary key for the TelemetryProperty entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column @JsonIgnore Long telPropId;
    @Column String telPropName;
    @Column String telPropValue;
    @Column @JsonIgnore TelemetryPropertyTypeEnum telPropType;

    /**
     * Telemetry item associated with this telemetry property.
     */
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "tel_id") @JsonIgnore private TelemetryItem telItem;
}