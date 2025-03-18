package com.bloodlink.repositories;

import com.bloodlink.entities.BankEmployee;
import com.bloodlink.entities.MedicalEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankEmployeeRepository extends JpaRepository<BankEmployee, Long> {
    Optional<BankEmployee> findByEmail(String email);
}
