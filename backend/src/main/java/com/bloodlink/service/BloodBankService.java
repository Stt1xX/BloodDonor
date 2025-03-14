package com.bloodlink.service;

import com.bloodlink.entities.BloodBank;
import com.bloodlink.entities.DTOs.OrganizationDTO;
import com.bloodlink.repositories.BloodBankRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BloodBankService {
    private final BloodBankRepository bloodBankRepository;

    @Transactional(readOnly = true)
    public List<BloodBank> findAll() {
        return bloodBankRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BloodBank findById(Long id) {
        return bloodBankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Банк крови не найден"));
    }

    @Transactional(readOnly = true)
    public List<BloodBank> searchByName(String name) {
        return bloodBankRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<BloodBank> findBanksWithAvailableBlood(String bloodType, String rhFactor, Double volume) {
        return bloodBankRepository.findBanksWithAvailableBlood(bloodType, rhFactor, volume);
    }

    @Transactional
    public ResponseEntity<?> save(OrganizationDTO organizationDTO) {
        bloodBankRepository.save(organizationDTO.getBloodBank());
        return new ResponseEntity<>("Банк крови успешно добавлен", HttpStatus.OK);
    }

    @Transactional
    public void delete(Long id) {
        bloodBankRepository.deleteById(id);
    }
} 