package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyDonationStatus(
    Long devoteeId,
    String devoteeName,
    String financialYear,
    String month,
    BigDecimal defaultAmount,
    BigDecimal paidAmount,
    LocalDate donationDate,
    String paymentMode,
    String remarks,
    String status
) {}