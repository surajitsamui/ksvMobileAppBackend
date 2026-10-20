package com.example.demo.dto;

import java.math.BigDecimal;

public record DashboardSummary(
    String financialYear,
    String month,
    BigDecimal totalDonation,
    BigDecimal totalExpense,
    BigDecimal balance
) {}