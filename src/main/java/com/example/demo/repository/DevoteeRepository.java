package com.example.demo.repository;


import com.example.demo.model.Devotee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevoteeRepository extends JpaRepository<Devotee, Long> {

    List<Devotee> findAllByOrderByNameAsc();

    List<Devotee> findByActiveTrueOrderByNameAsc();

    List<Devotee> findByActiveTrue();
}