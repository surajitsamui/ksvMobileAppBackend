package com.example.demo.controller;

import com.example.demo.dto.MonthlyReportResponse;
import com.example.demo.model.Donation;
import com.example.demo.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devotee")
@SecurityRequirement(name = "bearerAuth")
public class DevoteeReportController {

    private final ReportService reportService;

    public DevoteeReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Devotee checks if report for selected period is published
    @GetMapping("/reports/status")
    public Map<String, Object> getStatus(
            @RequestParam String financialYear,
            @RequestParam String month) {
        boolean published = reportService.isReportPublished(financialYear, month);
        Map<String, Object> res = new HashMap<>();
        res.put("financialYear", financialYear);
        res.put("month", month);
        res.put("published", published);
        return res;
    }

    // Devotee Dashboard (Returns temple monthly report ONLY IF published)
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(
            @RequestParam String financialYear,
            @RequestParam String month) {
        
        boolean published = reportService.isReportPublished(financialYear, month);
        if (!published) {
            Map<String, Object> err = new HashMap<>();
            err.put("published", false);
            err.put("message", "Report for " + month + " " + financialYear + " has not been published by Admin yet.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
        }

        MonthlyReportResponse report = reportService.getMonthlyReport(financialYear, month);
        return ResponseEntity.ok(report);
    }

    // Logged-in Devotee's own personal donation history
    @GetMapping("/donations")
    public List<Donation> getMyDonations(
            Authentication authentication,
            @RequestParam String financialYear) {
        String devoteeEmail = authentication.getName();
        return reportService.getDevoteeDonationsByEmail(devoteeEmail, financialYear);
    }
}