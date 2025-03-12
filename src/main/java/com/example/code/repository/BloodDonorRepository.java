package com.example.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.code.model.BloodDonor;

import java.util.List;

public interface BloodDonorRepository extends JpaRepository<BloodDonor, Long> {
    List<BloodDonor> findByBloodType(String bloodType);
    List<BloodDonor> findByIsAvailable(boolean isAvailable);
    
    @Query("SELECT b FROM BloodDonor b WHERE b.bloodType = :bloodType AND b.isAvailable = true")
    List<BloodDonor> findAvailableDonorsByBloodType(@Param("bloodType") String bloodType);
} 