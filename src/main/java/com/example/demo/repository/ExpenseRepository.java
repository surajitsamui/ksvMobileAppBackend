package com.example.demo.repository;

import com.example.demo.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByFinancialYearAndMonthOrderByExpenseDateDesc(
        String financialYear,
        String month
    );

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.financialYear = :fy
          AND e.month = :month
        """)
    BigDecimal getTotalExpense(
        @Param("fy") String financialYear,
        @Param("month") String month
    );

    List<Expense> findByFinancialYear(String financialYear);
}