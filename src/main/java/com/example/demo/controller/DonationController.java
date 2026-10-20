
package com.example.demo.controller;

import com.example.demo.dto.DonationRequest;
import com.example.demo.dto.DonationResponse;
import com.example.demo.service.DonationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/donations")
@SecurityRequirement(name = "bearerAuth")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    // Get all donation records for one devotee and financial year
    @GetMapping
    public List<DonationResponse> getYearlyDonations(
            @RequestParam Long devoteeId,
            @RequestParam String financialYear) {

        return donationService.getYearlyDonations(
                devoteeId, financialYear);
    }

    // Get one month's donation
    @GetMapping("/lookup")
    public DonationResponse getMonthlyDonation(
            @RequestParam Long devoteeId,
            @RequestParam String financialYear,
            @RequestParam String month) {

        return donationService.getMonthlyDonation(
                devoteeId, financialYear, month);
    }

    // Create a donation, or update the existing month record
    @PostMapping
    public ResponseEntity<DonationResponse> saveOrUpdate(
            @Valid @RequestBody DonationRequest request) {

        DonationResponse response =
                donationService.saveOrUpdate(request);

        return ResponseEntity.ok(response);
    }


}
