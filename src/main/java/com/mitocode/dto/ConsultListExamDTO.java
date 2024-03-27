package com.mitocode.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultListExamDTO {

    @NotNull
    private ConsultDTO consult;

    @NotNull
    private List<ExamDTO> lstExam;
}
