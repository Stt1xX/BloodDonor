package com.bloodlink.service;

import com.bloodlink.entities.MedicalInstitution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
public class MedicalInstitutionService extends OrganizationService<MedicalInstitution> {

    protected MedicalInstitutionService(JpaRepository<MedicalInstitution, Long> repository, JpaSpecificationExecutor<MedicalInstitution> specificationExecutor) {
        super(repository, specificationExecutor);
    }
}