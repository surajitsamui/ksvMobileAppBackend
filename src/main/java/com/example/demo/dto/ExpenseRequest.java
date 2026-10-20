package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}")
    String financialYear,

    @NotBlank
    String month,

    @NotBlank
    @Size(max = 200)
    String description,

    @NotNull
    @DecimalMin("0.01")
    BigDecimal amount,

    @NotNull
    LocalDate expenseDate,

    @Size(max = 500)
    String remarks

) {}