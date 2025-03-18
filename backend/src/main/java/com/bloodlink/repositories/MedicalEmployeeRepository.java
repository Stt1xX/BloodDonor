package com.bloodlink.repositories;

import com.bloodlink.entities.MedicalEmployee;
import com.bloodlink.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalEmployeeRepository extends JpaRepository<MedicalEmployee, Long> {
    Optional<MedicalEmployee> findByEmail(String email);
}
