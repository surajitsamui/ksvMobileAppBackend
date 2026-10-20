
package com.example.demo.repository;

import com.example.demo.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DonationRepository
        extends JpaRepository<Donation, Long> {

    List<Donation> findByDevotee_IdAndFinancialYearOrderByIdAsc(
            Long devoteeId,
            String financialYear
    );

    Optional<Donation> findByDevotee_IdAndFinancialYearAndMonth(
            Long devoteeId,
            String financialYear,
            String month
    );

    boolean existsByDevotee_IdAndFinancialYearAndMonthAndIdNot(
            Long devoteeId,
            String financialYear,
            String month,
            Long id
    );

    @Query("""
    SELECT COALESCE(SUM(d.amount), 0)
    FROM Donation d
    WHERE d.financialYear = :fy
      AND d.month = :month
    """)
    BigDecimal getTotalDonation(
            @Param("fy") String financialYear,
            @Param("month") String month
    );

    List<Donation> findByFinancialYearAndMonth(
            String financialYear, String month);

    List<Donation> findByFinancialYear(String financialYear);
}
