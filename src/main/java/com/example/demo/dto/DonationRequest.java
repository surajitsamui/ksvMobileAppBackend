
package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationRequest(

        @NotNull(message = "Devotee ID is required")
        @Positive(message = "Invalid devotee ID")
        Long devoteeId,

        @NotBlank(message = "Financial year is required")
        @Pattern(
            regexp = "\\d{4}-\\d{2}",
            message = "Financial year must look like 2026-27"
        )
        String financialYear,

        @NotBlank(message = "Month is required")
        @Pattern(
            regexp = "April|May|June|July|August|September|October|November|December|January|February|March",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid financial-year month"
        )
        String month,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 10, fraction = 2,
                message = "Amount can have at most two decimal places")
        BigDecimal amount,

        @NotNull(message = "Donation date is required")
        @PastOrPresent(message = "Donation date cannot be in the future")
        LocalDate donationDate,

        @NotBlank(message = "Payment mode is required")
        @Pattern(
            regexp = "Cash|UPI|Bank Transfer|Cheque",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid payment mode"
        )
        String paymentMode,

        @Size(max = 500, message = "Remarks cannot exceed 500 characters")
        String remarks
) {
}
