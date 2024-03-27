package com.mitocode.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record CustomErrorResponse(
        LocalDate dateTime,
        String message,
        String details
) {

}
