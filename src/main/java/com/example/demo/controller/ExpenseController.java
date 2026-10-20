package com.example.demo.controller;

import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseSummary;
import com.example.demo.model.Expense;
import com.example.demo.service.ExpenseService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devotee/expenses")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Get expenses for a selected month
    @GetMapping
    public List<Expense> getExpenses(
        @RequestParam String financialYear,
        @RequestParam String month
    ) {
        return expenseService.getExpenses(financialYear, month);
    }

    // Get total expense for a selected month
    @GetMapping("/total")
    public ExpenseSummary getTotalExpense(
        @RequestParam String financialYear,
        @RequestParam String month
    ) {
        return expenseService.getSummary(financialYear, month);
    }


}