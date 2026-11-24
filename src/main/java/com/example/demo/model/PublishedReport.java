package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "published_reports", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"financialYear", "month"})
})
public class PublishedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String financialYear; // e.g. "2026-27"

    @Column(nullable = false)
    private String month;         // e.g. "October"

    @Column(nullable = false)
    private boolean published;

    public PublishedReport() {}

    public PublishedReport(String financialYear, String month, boolean published) {
        this.financialYear = financialYear;
        this.month = month;
        this.published = published;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFinancialYear() { return financialYear; }
    public void setFinancialYear(String financialYear) { this.financialYear = financialYear; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}