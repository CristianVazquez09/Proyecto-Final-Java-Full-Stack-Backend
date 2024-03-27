package com.mitocode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Consult {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer idConsult;

    @ManyToOne //FK
    @JoinColumn(name = "id_patient", nullable = false, foreignKey = @ForeignKey(name="FK_CONSULT_PATIENT"))
    private Patient patient;

    @ManyToOne //FK
    @JoinColumn(name = "id_medic", nullable = false, foreignKey = @ForeignKey(name="FK_CONSULT_MEDIC"))
    private Medic medic;

    @ManyToOne //FK
    @JoinColumn(name = "id_speciality", nullable = false, foreignKey = @ForeignKey(name="FK_CONSULT_SPECIALITY"))
    private Speciality speciality;

    @Column(nullable = false, length = 3)
    private String numConsult;

    @Column(nullable = false)
    private LocalDateTime consultDate;

    @OneToMany(mappedBy = "consult", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ConsultDetail> details;
}
