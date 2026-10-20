package com.example.demo.dto;

import com.example.demo.model.Donation;
import com.example.demo.model.Expense;
import java.math.BigDecimal;
import java.util.List;

public record MonthlyReportResponse(
    String organization,
    String financialYear,
    String month,
    int paidCount,
    int pendingCount,
    BigDecimal totalDonation,
    BigDecimal totalExpense,
    BigDecimal balance,
    List<Donation> donations,
    List<Expense> expenses
) {}