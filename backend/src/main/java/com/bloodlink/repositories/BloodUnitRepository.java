package com.bloodlink.repositories;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.BloodType;
import com.bloodlink.entities.RhFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {
    List<BloodUnit> findByBloodTypeAndRhFactor(BloodType bloodType, RhFactor rhFactor);
    
    List<BloodUnit> findByBloodBankId(Long bloodBankId);
    
    @Query("SELECT bu FROM BloodUnit bu WHERE bu.expirationDate <= :date")
    List<BloodUnit> findExpiringUnits(LocalDateTime date);
    
    @Query("SELECT SUM(bu.volume) FROM BloodUnit bu WHERE bu.bloodType = :bloodType AND bu.rhFactor = :rhFactor AND bu.bloodBank.id = :bankId")
    Double getTotalVolumeByTypeAndBank(BloodType bloodType, RhFactor rhFactor, Long bankId);
} 