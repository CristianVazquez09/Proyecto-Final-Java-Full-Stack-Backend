package com.mitocode.dto;

import com.mitocode.model.Patient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VitalSignDTO {

    @EqualsAndHashCode.Include
    private Integer idVitalSign;

    @NotNull
    private PatientDTO patient;

    @NotNull
    private LocalDateTime dateVitalSign;

    @NotNull
    private String temperature;

    @NotNull
    private String heartbeat;

    @NotNull
    private String respiratoryRate;
}
