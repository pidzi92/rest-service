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
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "telemetry_item", schema = "telemetry")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelemetryItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tel_id") @JsonIgnore Long telId;
    @OneToMany(mappedBy = "telItem", cascade = CascadeType.ALL, orphanRemoval = true) private List<TelemetryProperty> telProps;
}
