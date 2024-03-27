package com.mitocode.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialityDTO {

    @EqualsAndHashCode.Include
    private Integer idSpeciality;


    @NotNull
    private String nameSpeciality;

    @NotNull
    private String descriptionSpeciality;
}
