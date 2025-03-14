package com.bloodlink.service;

import com.bloodlink.entities.DTOs.OrganizationDTO;
import com.bloodlink.entities.MedicalInstitution;
import com.bloodlink.repositories.MedicalInstitutionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalInstitutionService {
    private final MedicalInstitutionRepository medicalInstitutionRepository;

    @Transactional(readOnly = true)
    public List<MedicalInstitution> findAll() {
        return medicalInstitutionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MedicalInstitution findById(Long id) {
        return medicalInstitutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Медицинское учреждение не найдено"));
    }

    @Transactional(readOnly = true)
    public List<MedicalInstitution> searchByName(String name) {
        return medicalInstitutionRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public ResponseEntity<?> save(OrganizationDTO organizationDTO) {
        medicalInstitutionRepository.save(organizationDTO.getMedicalInstitution());
        return new ResponseEntity<>("Медецинское учреждение успешно добавлено", HttpStatus.OK);
    }

    @Transactional
    public void delete(Long id) {
        medicalInstitutionRepository.deleteById(id);
    }
} 