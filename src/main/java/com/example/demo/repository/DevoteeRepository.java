package com.example.demo.repository;


import com.example.demo.model.Devotee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DevoteeRepository extends JpaRepository<Devotee, Long> {

    List<Devotee> findAllByOrderByNameAsc();

    List<Devotee> findByActiveTrueOrderByNameAsc();

    List<Devotee> findByActiveTrue();

    // NEW: Find devotee by email for authentication & devotee dashboard
    Optional<Devotee> findByEmail(String email);
}