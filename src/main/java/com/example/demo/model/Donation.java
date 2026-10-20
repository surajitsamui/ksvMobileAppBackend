
package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "donations",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_donation_devotee_fy_month",
        columnNames = {"devotee_id", "financial_year", "month"}
    )
)
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "devotee_id", nullable = false)
    private Devotee devotee;

    @Column(name = "financial_year", nullable = false, length = 7)
    private String financialYear;

    @Column(nullable = false, length = 20)
    private String month;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "donation_date", nullable = false)
    private LocalDate donationDate;

    @Column(name = "payment_mode", nullable = false, length = 30)
    private String paymentMode;

    @Column(length = 500)
    private String remarks;

    public Donation() {}

    public Long getId() { return id; }
    public Devotee getDevotee() { return devotee; }
    public void setDevotee(Devotee devotee) { this.devotee = devotee; }
    public String getFinancialYear() { return financialYear; }
    public void setFinancialYear(String financialYear) { this.financialYear = financialYear; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
