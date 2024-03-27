package com.mitocode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class VitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idVitalSign;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false, foreignKey = @ForeignKey(name="FK_VITAL-SINGS_PATIENT"))
    private Patient patient;

    @Column (nullable = false)
    private LocalDateTime dateVitalSign;

    @Column (nullable = false, length = 10)
    private String temperature;

    @Column (nullable = false, length = 10)
    private String heartbeat;

    @Column (nullable = false, length = 70)
    private String respiratoryRate;


}
