package com.example.demo.service;

import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseSummary;
import com.example.demo.model.Expense;
import com.example.demo.repository.ExpenseRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private static final List<String> MONTHS = List.of(
        "April", "May", "June", "July", "August", "September",
        "October", "November", "December", "January",
        "February", "March"
    );

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional(readOnly = true)
    public List<Expense> getExpenses(String fy, String month) {
        validatePeriod(fy, month);

        return expenseRepository
            .findByFinancialYearAndMonthOrderByExpenseDateDesc(
                fy, month
            );
    }

    @Transactional(readOnly = true)
    public ExpenseSummary getSummary(String fy, String month) {
        validatePeriod(fy, month);

        BigDecimal total = expenseRepository.getTotalExpense(
            fy, month
        );

        return new ExpenseSummary(fy, month, total);
    }

    @Transactional
    public Expense addExpense(ExpenseRequest request) {
        validatePeriod(request.financialYear(), request.month());

        Expense expense = new Expense();
        expense.setFinancialYear(request.financialYear());
        expense.setMonth(request.month());
        expense.setDescription(request.description().trim());
        expense.setAmount(request.amount());
        expense.setExpenseDate(request.expenseDate());
        expense.setRemarks(request.remarks());

        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Expense not found"
            );
        }

        expenseRepository.deleteById(id);
    }

    private void validatePeriod(String fy, String month) {
        if (fy == null ||
            !fy.matches("\\d{4}-\\d{2}") ||
            !MONTHS.contains(month)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid financial year or month"
            );
        }

        int startYear = Integer.parseInt(fy.substring(0, 4));
        int endYear = Integer.parseInt(fy.substring(5, 7));

        if (endYear != (startYear + 1) % 100) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid financial year"
            );
        }
    }
}