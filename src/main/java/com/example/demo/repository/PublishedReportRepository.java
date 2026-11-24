package com.example.demo.repository;



import com.example.demo.model.PublishedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PublishedReportRepository extends JpaRepository<PublishedReport, Long> {
    Optional<PublishedReport> findByFinancialYearAndMonthIgnoreCase(String financialYear, String month);
    boolean existsByFinancialYearAndMonthIgnoreCaseAndPublishedTrue(String financialYear, String month);
}
