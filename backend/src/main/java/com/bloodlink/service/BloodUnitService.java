package com.bloodlink.service;

import com.bloodlink.entities.BloodUnit;
import com.bloodlink.entities.BloodType;
import com.bloodlink.entities.RhFactor;
import com.bloodlink.repositories.BloodUnitRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodUnitService {
    private final BloodUnitRepository bloodUnitRepository;

    @Transactional(readOnly = true)
    public List<BloodUnit> findByTypeAndRhFactor(BloodType bloodType, RhFactor rhFactor) {
        return bloodUnitRepository.findByBloodTypeAndRhFactor(bloodType, rhFactor);
    }

    @Transactional(readOnly = true)
    public List<BloodUnit> findByBloodBank(Long bloodBankId) {
        return bloodUnitRepository.findByBloodBankId(bloodBankId);
    }

    @Transactional(readOnly = true)
    public List<BloodUnit> findExpiringUnits(LocalDateTime date) {
        return bloodUnitRepository.findExpiringUnits(date);
    }

    @Transactional(readOnly = true)
    public Double getTotalVolumeByTypeAndBank(BloodType bloodType, RhFactor rhFactor, Long bankId) {
        return bloodUnitRepository.getTotalVolumeByTypeAndBank(bloodType, rhFactor, bankId);
    }

    @Transactional
    public BloodUnit save(BloodUnit bloodUnit) {
        return bloodUnitRepository.save(bloodUnit);
    }

    @Transactional
    public void delete(Long id) {
        bloodUnitRepository.deleteById(id);
    }
} 