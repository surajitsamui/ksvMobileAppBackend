package com.example.demo.service;

import com.example.demo.dto.MonthlyReportResponse;
import com.example.demo.dto.YearlyReportResponse;
import com.example.demo.model.Devotee;
import com.example.demo.model.Donation;
import com.example.demo.model.Expense;
import com.example.demo.model.PublishedReport;
import com.example.demo.repository.DevoteeRepository;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.PublishedReportRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReportService {

    private final DevoteeRepository devoteeRepository;
    private final DonationRepository donationRepository;
    private final ExpenseRepository expenseRepository;
    private final PublishedReportRepository publishedReportRepository; // NEW

    private static final String ORGANIZATION = "KANDUAH SATSANG VIHAR";

    private static final List<String> MONTHS = List.of(
            "April", "May", "June", "July", "August", "September",
            "October", "November", "December", "January",
            "February", "March"
    );

    public ReportService(
            DevoteeRepository devoteeRepository,
            DonationRepository donationRepository,
            ExpenseRepository expenseRepository,
            PublishedReportRepository publishedReportRepository) { // UPDATED CONSTRUCTOR
        this.devoteeRepository = devoteeRepository;
        this.donationRepository = donationRepository;
        this.expenseRepository = expenseRepository;
        this.publishedReportRepository = publishedReportRepository;
    }

    @Transactional(readOnly = true)
    public MonthlyReportResponse getMonthlyReport(
            String fy, String month) {

        validatePeriod(fy, month);

        List<Donation> donations =
                donationRepository.findByFinancialYearAndMonth(fy, month);

        List<Expense> expenses =
                expenseRepository
                        .findByFinancialYearAndMonthOrderByExpenseDateDesc(fy, month);

        List<Devotee> activeDevotees =
                devoteeRepository.findByActiveTrue();

        BigDecimal totalDonation = donations.stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int paidCount = (int) donations.stream()
                .filter(d -> activeDevotees.stream()
                        .anyMatch(v -> v.getId().equals(d.getDevotee().getId())))
                .count();

        int pendingCount = Math.max(
                0, activeDevotees.size() - paidCount);

        return new MonthlyReportResponse(
                ORGANIZATION,
                fy,
                month,
                paidCount,
                pendingCount,
                totalDonation,
                totalExpense,
                totalDonation.subtract(totalExpense),
                donations,
                expenses
        );
    }

    @Transactional(readOnly = true)
    public YearlyReportResponse getYearlyReport(String fy) {
        validateFinancialYear(fy);

        return new YearlyReportResponse(
                ORGANIZATION,
                fy,
                devoteeRepository.findAll(),
                donationRepository.findByFinancialYear(fy),
                expenseRepository.findByFinancialYear(fy)
        );
    }

    @Transactional(readOnly = true)
    public List<Devotee> getDevoteesMaster() {
        return devoteeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Donation> getDonationsMaster() {
        return donationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpensesMaster() {
        return expenseRepository.findAll();
    }

    // =========================================================================
    // NEW METHODS FOR REPORT PUBLISHING & DEVOTEE VIEW
    // =========================================================================

    @Transactional(readOnly = true)
    public boolean isReportPublished(String fy, String month) {
        validatePeriod(fy, month);
        return publishedReportRepository
                .findByFinancialYearAndMonthIgnoreCase(fy, month)
                .map(PublishedReport::isPublished)
                .orElse(false);
    }

    @Transactional
    public void setReportPublished(String fy, String month, boolean published) {
        validatePeriod(fy, month);

        PublishedReport report = publishedReportRepository
                .findByFinancialYearAndMonthIgnoreCase(fy, month)
                .orElse(new PublishedReport(fy, month, published));

        report.setPublished(published);
        publishedReportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<Donation> getDevoteeDonationsByEmail(String email, String fy) {
        validateFinancialYear(fy);

        Devotee devotee = devoteeRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Devotee account not found for email: " + email));

        return donationRepository.findByDevoteeIdAndFinancialYear(devotee.getId(), fy);
    }

    // =========================================================================
    // PRIVATE VALIDATION HELPERS
    // =========================================================================

    private void validatePeriod(String fy, String month) {
        validateFinancialYear(fy);

        if (month == null || !MONTHS.contains(month)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid month");
        }
    }

    private void validateFinancialYear(String fy) {
        if (fy == null || !fy.matches("\\d{4}-\\d{2}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid financial year");
        }

        int start = Integer.parseInt(fy.substring(0, 4));
        int end = Integer.parseInt(fy.substring(5, 7));

        if (end != (start + 1) % 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid financial year");
        }
    }
}