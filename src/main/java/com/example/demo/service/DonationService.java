
package com.example.demo.service;

import com.example.demo.dto.DonationRequest;
import com.example.demo.dto.DonationResponse;
import com.example.demo.model.Devotee;
import com.example.demo.model.Donation;
import com.example.demo.repository.DevoteeRepository;
import com.example.demo.repository.DonationRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class DonationService {

    private final DonationRepository donations;
    private final DevoteeRepository devotees;

    public DonationService(
            DonationRepository donations,
            DevoteeRepository devotees) {
        this.donations = donations;
        this.devotees = devotees;
    }

    @Transactional(readOnly = true)
    public List<DonationResponse> getYearlyDonations(
            Long devoteeId, String financialYear) {

        return donations
                .findByDevotee_IdAndFinancialYearOrderByIdAsc(
                        devoteeId, financialYear)
                .stream()
                .map(DonationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DonationResponse getMonthlyDonation(
            Long devoteeId, String financialYear, String month) {

        Donation donation = donations
                .findByDevotee_IdAndFinancialYearAndMonth(
                        devoteeId, financialYear, normalizeMonth(month))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Donation record not found"));

        return DonationResponse.from(donation);
    }

    @Transactional
    public DonationResponse saveOrUpdate(DonationRequest request) {

        Devotee devotee = getActiveDevotee(request.devoteeId());
        String month = normalizeMonth(request.month());

        Donation donation = donations
                .findByDevotee_IdAndFinancialYearAndMonth(
                        devotee.getId(),
                        request.financialYear(),
                        month)
                .orElseGet(Donation::new);

        donation.setDevotee(devotee);
        applyRequest(donation, request, month);

        return DonationResponse.from(donations.save(donation));
    }

    @Transactional
    public DonationResponse update(
            Long id, DonationRequest request) {

        Donation donation = donations.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Donation not found"));

        Devotee devotee = getActiveDevotee(request.devoteeId());
        String month = normalizeMonth(request.month());

        boolean duplicate =
                donations.existsByDevotee_IdAndFinancialYearAndMonthAndIdNot(
                        devotee.getId(),
                        request.financialYear(),
                        month,
                        id);

        if (duplicate) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A donation already exists for this devotee, year and month");
        }

        donation.setDevotee(devotee);
        applyRequest(donation, request, month);

        return DonationResponse.from(donations.save(donation));
    }

    @Transactional
    public void delete(Long id) {
        Donation donation = donations.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Donation not found"));

        donations.delete(donation);
    }

    private Devotee getActiveDevotee(Long devoteeId) {
        Devotee devotee = devotees.findById(devoteeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Devotee not found"));

        if (!devotee.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot record a donation for an inactive devotee");
        }

        return devotee;
    }

    private void applyRequest(
            Donation donation,
            DonationRequest request,
            String month) {

        donation.setFinancialYear(request.financialYear());
        donation.setMonth(month);
        donation.setAmount(request.amount());
        donation.setDonationDate(request.donationDate());
        donation.setPaymentMode(request.paymentMode().trim());
        donation.setRemarks(request.remarks());
    }

    private String normalizeMonth(String month) {
        if (month == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Month is required");
        }

        String normalized = month.trim().toLowerCase(Locale.ROOT);

        return switch (normalized) {
            case "april" -> "April";
            case "may" -> "May";
            case "june" -> "June";
            case "july" -> "July";
            case "august" -> "August";
            case "september" -> "September";
            case "october" -> "October";
            case "november" -> "November";
            case "december" -> "December";
            case "january" -> "January";
            case "february" -> "February";
            case "march" -> "March";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid month");
        };
    }
}
