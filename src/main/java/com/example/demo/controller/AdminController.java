package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.AppUser;
import com.example.demo.model.Devotee;
import com.example.demo.model.Expense;
import com.example.demo.model.Role;
import com.example.demo.repository.DevoteeRepository;
import com.example.demo.repository.DonationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DevoteeService;
import com.example.demo.service.DonationService;
import com.example.demo.service.ExpenseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final DevoteeService service;

    private final ExpenseService expenseService;
    private final DonationService donationService;

    private final DevoteeRepository devotees;
    private final UserRepository users;
    private final DonationRepository donations;
    private final PasswordEncoder encoder;
    public AdminController(DonationService donationService, DevoteeService service, ExpenseService expenseService, DevoteeRepository devotees, UserRepository users, DonationRepository donations, PasswordEncoder encoder) {
        this.service=service;
        this.donationService=donationService;
        this.expenseService = expenseService;
        this.devotees=devotees;
        this.users=users;
        this.donations=donations;
        this.encoder=encoder;
    }
    @GetMapping("/devotees") public List<Devotee> devotees(){
        return devotees.findAll();
    }

    @GetMapping("/devotees/{id}")
    public Devotee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/devotees/search")
    public List<Devotee> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "true")
            boolean includeInactive) {

        return service.search(query, includeInactive);
    }

    @PostMapping("/devotees")
    public Map<String,Object> createDevotee( @Valid @RequestBody DevoteeRequest request) {
        Devotee devotee = service.create(request);
        AppUser u=users.save(new AppUser(request.email(),encoder.encode(request.temporaryPassword()), Role.DEVOTEE,request.name(),request.mobile(),devotee ));
        return Map.of("devoteeId",devotee.getId(),"userId",u.getId(),"message","Devotee created");
    }


    @PutMapping("/devotees/{id}")
    public Devotee update( @PathVariable Long id, @RequestBody DevoteeRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/devotees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteDevotee(@PathVariable Long id) {
        deleteIfNoDonations(id);
    }

    @GetMapping("/donations")
    public List<DonationResponse> getYearlyDonations(
            @RequestParam Long devoteeId,
            @RequestParam String financialYear) {

        return donationService.getYearlyDonations(
                devoteeId, financialYear);
    }
    @PostMapping("/donations")
    public ResponseEntity<DonationResponse> saveOrUpdate(@Valid @RequestBody DonationRequest request) {
        DonationResponse response = donationService.saveOrUpdate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lookup")
    public DonationResponse getMonthlyDonation(
            @RequestParam Long devoteeId,
            @RequestParam String financialYear,
            @RequestParam String month) {

        return donationService.getMonthlyDonation(
                devoteeId, financialYear, month);
    }
    // Update a particular donation by ID
    @PutMapping("/donations/{id}")
    public DonationResponse update(
            @PathVariable Long id,
            @Valid @RequestBody DonationRequest request) {

        return donationService.update(id, request);
    }

    // Delete a donation record
    @DeleteMapping("/donations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        donationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expenses")
    public List<Expense> getExpenses(
            @RequestParam String financialYear,
            @RequestParam String month
    ) {
        return expenseService.getExpenses(financialYear, month);
    }

    // Get total expense for a selected month
    @GetMapping("/expenses/total")
    public ExpenseSummary getTotalExpense(
            @RequestParam String financialYear,
            @RequestParam String month
    ) {
        return expenseService.getSummary(financialYear, month);
    }
    // Add an expense
    @PostMapping("/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense addExpense(
            @Valid @RequestBody ExpenseRequest request
    ) {
        return expenseService.addExpense(request);
    }

    // Delete an expense
    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteIfNoDonations(Long devoteeId) {

        Devotee devotee = devotees.findById(devoteeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Devotee not found"
                ));

        boolean hasDonations = donations.hasDonations(devoteeId);

        if (hasDonations) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete devotee because donation records exist"
            );
        }

        // Find any login account associated with this devotee
        users.findById(devoteeId)
                .ifPresent(users::delete);

        // Delete devotee only after checking donations and removing
        // the associated login account
        devotees.delete(devotee);
    }
}




