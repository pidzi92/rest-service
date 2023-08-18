package com.telemetry.restservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tel_id") Long telId;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "vehicle_id") @JsonIgnore private Vehicle vehicle;
    @OneToMany(mappedBy = "telItem", cascade = CascadeType.ALL, orphanRemoval = true) private List<TelemetryProperty> telProps;
}
