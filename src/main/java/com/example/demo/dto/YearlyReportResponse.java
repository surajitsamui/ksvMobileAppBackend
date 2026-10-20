package com.example.demo.dto;

import com.example.demo.model.Devotee;
import com.example.demo.model.Donation;
import com.example.demo.model.Expense;
import java.util.List;

public record YearlyReportResponse(
    String organization,
    String financialYear,
    List<Devotee> devotees,
    List<Donation> donations,
    List<Expense> expenses
) {}