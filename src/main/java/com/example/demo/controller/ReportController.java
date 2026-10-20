package com.example.demo.controller;

import com.example.demo.dto.MonthlyReportResponse;
import com.example.demo.dto.YearlyReportResponse;
import com.example.demo.model.Devotee;
import com.example.demo.model.Donation;
import com.example.demo.model.Expense;
import com.example.demo.service.ReportService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Monthly financial statement
    @GetMapping("/monthly")
    public MonthlyReportResponse monthlyReport(
            @RequestParam String financialYear,
            @RequestParam String month) {
        return reportService.getMonthlyReport(financialYear, month);
    }

    // Annual report
    @GetMapping("/yearly")
    public YearlyReportResponse yearlyReport(
            @RequestParam String financialYear) {
        return reportService.getYearlyReport(financialYear);
    }

    // Master reports
    @GetMapping("/master/devotees")
    public List<Devotee> devoteesMaster() {
        return reportService.getDevoteesMaster();
    }

    @GetMapping("/master/donations")
    public List<Donation> donationsMaster() {
        return reportService.getDonationsMaster();
    }

    @GetMapping("/master/expenses")
    public List<Expense> expensesMaster() {
        return reportService.getExpensesMaster();
    }
}