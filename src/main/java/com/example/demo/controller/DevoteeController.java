package com.example.demo.controller;


import com.example.demo.dto.DevoteeRequest;
import com.example.demo.dto.StatusRequest;
import com.example.demo.model.Devotee;
import com.example.demo.service.DevoteeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/devotees")
@SecurityRequirement(name = "bearerAuth")
public class DevoteeController {

    private final DevoteeService service;

    public DevoteeController(DevoteeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Devotee> getAll(
            @RequestParam(defaultValue = "true")
            boolean includeInactive) {

        return service.getAll(includeInactive);
    }

    @GetMapping("/{id}")
    public Devotee getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<Devotee> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "true")
            boolean includeInactive) {

        return service.search(query, includeInactive);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Devotee create(@RequestBody DevoteeRequest request) {
//        return service.create(request);
//    }

//    @PutMapping("/{id}")
//    public Devotee update(
//            @PathVariable Long id,
//            @RequestBody DevoteeRequest request) {
//
//        return service.update(id, request);
//    }

    @PatchMapping("/{id}/status")
    public Devotee updateStatus(
            @PathVariable Long id,
            @RequestBody StatusRequest request) {

        if (request == null || request.active() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Active status is required");
        }

        return service.updateStatus(id, request.active());
    }


}