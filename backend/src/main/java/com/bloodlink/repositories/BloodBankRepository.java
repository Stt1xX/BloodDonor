package com.bloodlink.repositories;

import com.bloodlink.entities.BloodBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBank, Long> {
    List<BloodBank> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT DISTINCT bb FROM BloodBank bb JOIN bb.bloodUnits bu WHERE bu.bloodType = :bloodType AND bu.rhFactor = :rhFactor AND bu.volume >= :volume")
    List<BloodBank> findBanksWithAvailableBlood(String bloodType, String rhFactor, Double volume);
} 