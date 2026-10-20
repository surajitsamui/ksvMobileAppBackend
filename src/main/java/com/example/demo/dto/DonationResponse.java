
package com.example.demo.dto;

import com.example.demo.model.Donation;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DonationResponse(
        Long id,
        Long devoteeId,
        String devoteeName,
        String financialYear,
        String month,
        BigDecimal amount,
        LocalDate donationDate,
        String paymentMode,
        String remarks
) {
    public static DonationResponse from(Donation donation) {
        return new DonationResponse(
                donation.getId(),
                donation.getDevotee().getId(),
                donation.getDevotee().getName(),
                donation.getFinancialYear(),
                donation.getMonth(),
                donation.getAmount(),
                donation.getDonationDate(),
                donation.getPaymentMode(),
                donation.getRemarks()
        );
    }
}
