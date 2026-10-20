package com.example.demo.dto;

import java.math.BigDecimal;

public record ExpenseSummary(
    String financialYear,
    String month,
    BigDecimal totalExpense
) {}