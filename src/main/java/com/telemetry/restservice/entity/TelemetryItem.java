package com.telemetry.restservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Entity class representing telemetry data items stored in the database.
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryItem implements Serializable {
    /**
     * Primary key for the TelemetryItem entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column @JsonIgnore Long telId;

    /**
     * List of telemetry properties associated with this telemetry item.
     */
    @OneToMany(mappedBy = "telItem", cascade = CascadeType.ALL, orphanRemoval = true) private List<TelemetryProperty> telProps;
}
