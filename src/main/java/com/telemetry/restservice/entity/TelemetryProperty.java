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
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryProperty implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tel_prop_id") Long telPropId;
    @Column(name="tel_prop_name") String telPropName;
    @Column(name="tel_prop_value") String telPropValue;
    @Column(name="tel_prop_type") TelemetryPropertyTypeEnum telPropType;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "tel_id") @JsonIgnore private TelemetryItem telItem;
}