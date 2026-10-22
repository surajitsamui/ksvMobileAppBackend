package com.example.demo.service;
import com.example.demo.dto.DevoteeRequest;
import com.example.demo.model.Devotee;
import com.example.demo.repository.DevoteeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class DevoteeService {

    private final DevoteeRepository repository;

    public DevoteeService(DevoteeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Devotee> getAll(boolean includeInactive) {
        return includeInactive
                ? repository.findAllByOrderByNameAsc()
                : repository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Devotee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Devotee not found: " + id));
    }

    public Devotee create(DevoteeRequest request) {
        validate(request);

        Devotee devotee = new Devotee();
        applyRequest(devotee, request, true);

        return repository.save(devotee);
    }

    public Devotee update(Long id, DevoteeRequest request) {
        validate(request);

        Devotee devotee = getById(id);
        applyRequest(devotee, request, false);

        return repository.save(devotee);
    }

    public Devotee updateStatus(Long id, boolean active) {
        Devotee devotee = getById(id);
        devotee.setActive(active);

        return repository.save(devotee);
    }

    public void delete(Long id) {
        Devotee devotee = getById(id);

        // Add related-donation handling before enabling hard deletion.
        // Do not delete financial records accidentally.
        repository.delete(devotee);
    }

    @Transactional(readOnly = true)
    public List<Devotee> search(String query, boolean includeInactive) {
        String term = query == null ? "" : query.trim().toLowerCase();

        return getAll(includeInactive).stream()
                .filter(d ->
                        contains(d.getName(), term)
                        || contains(d.getMobile(), term)
                        || contains(d.getAddress(), term))
                .toList();
    }

    private boolean contains(String value, String term) {
        return value != null
                && value.toLowerCase().contains(term);
    }

    private void applyRequest(Devotee devotee,DevoteeRequest request, boolean creating) {

        devotee.setName(request.name().trim());
        devotee.setMobile(normalize(request.mobile()));
        devotee.setAddress(normalize(request.address()));
        devotee.setMonthlyAmount(request.monthlyAmount());
        devotee.setEmail(request.email());

        if (request.active() != null) {
            devotee.setActive(request.active());
        } else if (creating) {
            devotee.setActive(true);
        }
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private void validate(DevoteeRequest request) {
        if (request == null
                || request.name() == null
                || request.name().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Devotee name is required");
        }

        if (request.name().trim().length() > 150) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name cannot exceed 150 characters");
        }

        if (request.monthlyAmount() == null
                || request.monthlyAmount()
                    .compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Monthly amount must be greater than zero");
        }

        if (request.mobile() != null
                && request.mobile().length() > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mobile number cannot exceed 20 characters");
        }

        if (request.address() != null
                && request.address().length() > 500) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Address cannot exceed 500 characters");
        }
    }


}