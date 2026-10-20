package com.example.demo.controller;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.service.DashboardService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardSummary getDashboard(
            @RequestParam String financialYear,
            @RequestParam String month) {

        return dashboardService.getSummary(financialYear, month);
    }
}