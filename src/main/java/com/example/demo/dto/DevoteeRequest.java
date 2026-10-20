package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record DevoteeRequest(

        String email,
        String name,
        String mobile,
        String address,
        BigDecimal monthlyAmount,
        Boolean active,
        @NotBlank String temporaryPassword
) {
}