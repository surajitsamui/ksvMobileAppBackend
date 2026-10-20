package com.example.demo.service;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.ExpenseRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final DonationRepository donationRepository;
    private final ExpenseRepository expenseRepository;

    private static final List<String> MONTHS = List.of(
        "April", "May", "June", "July", "August", "September",
        "October", "November", "December", "January",
        "February", "March"
    );

    public DashboardService(
            DonationRepository donationRepository,
            ExpenseRepository expenseRepository) {
        this.donationRepository = donationRepository;
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummary getSummary(
            String financialYear, String month) {

        validatePeriod(financialYear, month);

        BigDecimal donations = donationRepository.getTotalDonation(
            financialYear, month
        );

        BigDecimal expenses = expenseRepository.getTotalExpense(
            financialYear, month
        );

        if (donations == null) donations = BigDecimal.ZERO;
        if (expenses == null) expenses = BigDecimal.ZERO;

        BigDecimal balance = donations.subtract(expenses);

        return new DashboardSummary(
            financialYear,
            month,
            donations,
            expenses,
            balance
        );
    }

    private void validatePeriod(String fy, String month) {
        if (fy == null || !fy.matches("\\d{4}-\\d{2}")
                || !MONTHS.contains(month)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid financial year or month"
            );
        }

        int start = Integer.parseInt(fy.substring(0, 4));
        int end = Integer.parseInt(fy.substring(5, 7));

        if (end != (start + 1) % 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid financial year"
            );
        }
    }
}